package com.patrolnavi.firestore

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.patrolnavi.models.*
import com.patrolnavi.ui.activities.*
import com.patrolnavi.ui.fragments.CustomerListFragment
import com.patrolnavi.ui.fragments.MapsFragment
import com.patrolnavi.utils.Constants

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "新規登録エラー",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: LoginActivity) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                activity.userLoggedInSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ユーザー情報取り込みエラー",
                    e
                )
            }
    }

    fun getUserProfileDetails(activity: UserProfileActivity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)
                if (user != null) {
                    activity.userProfileGetSuccess(user)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ユーザー情報読み込みエラー",
                    e
                )
            }
    }

    fun getBelongingGroupsList(activity:UserProfileActivity){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .collection(Constants.BELONGING_GROUPS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val belongingGroupsList: ArrayList<BelongingGroups> = ArrayList()

                for (i in document.documents) {
                    val belongingGroups = i.toObject(BelongingGroups::class.java)!!
                    belongingGroups.groups_id = i.id
                    belongingGroupsList.add(belongingGroups)
                }
                activity.getBelongingGroupsListSuccess(belongingGroupsList)
                Log.i(
                    javaClass.simpleName,
                    "belongingGroupsList 読み込み完了"
                )
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "belongingGroupsList 読み込みエラー"
                )
            }
    }

    fun updateUserProfileData(
        activity: EditUserProfileActivity,
        userHashMap: HashMap<String, Any>
    ) {

        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {

                activity.userProfileUpdateSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ユーザー情報更新エラー",
                    e
                )
            }
    }

    fun uploadGroupsDetails(activity: AddGroupsActivity, groupsInfo: Groups) {

        mFireStore.collection(Constants.GROUPS)
            .document()
            .set(groupsInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.groupsUploadSuccess()
                Log.i(activity.javaClass.simpleName, "グループ登録完了")
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "グループ追加エラー", e)
            }
    }

    fun getGroupsList(activity: SettingGroupsActivity) {
        mFireStore.collection(Constants.GROUPS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val groupsList: ArrayList<Groups> = ArrayList()

                for (i in document.documents) {
                    val groups = i.toObject(Groups::class.java)!!
                    groups.groups_id = i.id
                    groupsList.add(groups)
                }
                activity.successGroupsList(groupsList)
                Log.i(
                    javaClass.simpleName,
                    "GroupsList 読み込み完了"
                )
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "GroupsList 読み込みエラー"
                )
            }
    }

    fun updateGroupsDetails(
        activity: EditGroupsActivity,
        groupsId: String,
        groupsHashMap: HashMap<String, Any>
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .update(groupsHashMap)
            .addOnSuccessListener {
                activity.groupsDetailsUpdateSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ユーザー情報更新エラー",
                    e
                )
            }
    }

    fun uploadGroupsUsers(
        activity: AddGroupsUsersActivity,
        groupsId: String,
        groupsUsers: GroupsUsers
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.GROUPS_USERS)
            .document(getCurrentUserID())
            .set(groupsUsers)
            .addOnSuccessListener {
                activity.groupsUsersUploadSuccess()
                Log.i(activity.javaClass.simpleName, "グループユーザー登録完了")
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "グループ追加エラー", e)
            }
    }

    fun getGroupsUsersList(activity: DetailsGroupsActivity, groupsId: String) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.GROUPS_USERS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val groupsUsersList: ArrayList<GroupsUsers> = ArrayList()

                for (i in document.documents) {
                    val groupsUsers = i.toObject(GroupsUsers::class.java)!!
                    groupsUsers.user_id = i.id
                    groupsUsersList.add(groupsUsers)
                }
                activity.successGroupsUsersList(groupsUsersList)
                Log.i(
                    javaClass.simpleName,
                    "GroupsUsersList 読み込み完了"
                )
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "GroupsUsersList 読み込みエラー"
                )
            }
    }

    fun getEditGroupsUsersList(activity: EditGroupsActivity, groupsId: String) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.GROUPS_USERS)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val groupsUsersList: ArrayList<GroupsUsers> = ArrayList()

                for (i in document.documents) {
                    val groupsUsers = i.toObject(GroupsUsers::class.java)!!
                    groupsUsers.user_id = i.id
                    groupsUsersList.add(groupsUsers)
                }
                activity.successEditGroupsUsersList(groupsUsersList)
                Log.i(
                    javaClass.simpleName,
                    "GroupsUsersList 読み込み完了"
                )
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "GroupsUsersList 読み込みエラー"
                )
            }
    }

    fun getEditGroupsUsersDetails(activity:EditGroupsUsersActivity,groupsId: String,userId:String){
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.GROUPS_USERS)
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val groupsUsers = document.toObject(GroupsUsers::class.java)
                if (groupsUsers != null) {
                    activity.groupsUsersDetailsSuccess(groupsUsers)
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "ユーザー情報取り込みエラー"
                )
            }
    }

    fun deleteBelongingGroupsUsers(activity: EditGroupsUsersActivity,groupsId: String,userId: String){
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.GROUPS_USERS)
            .document(userId)
            .delete()
            .addOnSuccessListener {
                activity.groupsUsersDeleteSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "お客様情報削除エラー", e)
            }

    }

    fun getJoinGroupsDetails(activity: JoinGroupsActivity, groupsId: String) {

        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val groups = document.toObject(Groups::class.java)
                if (groups != null) {
                    activity.successGroupsJoinDetails(groups)
                    Log.i(
                        javaClass.simpleName,
                        "Groups 読み込み完了"
                    )
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Groups 読み込みエラー"
                )
            }
    }

    fun JoinGroups(
        activity: JoinGroupsActivity,
        groupsHashMap: HashMap<String, Any>,
        groupsId: String
    ) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .collection(Constants.BELONGING_GROUPS_ID)
            .document(groupsId)
            .update(groupsHashMap)
            .addOnSuccessListener {
                activity.successGroupsJoin()
                Log.i(
                    javaClass.simpleName,
                    "Groups 読み込み完了"
                )
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Groups 読み込みエラー"
                )
            }
    }

    fun uploadCustomerDetails(
        activity: AddCustomerActivity,
        groupsId: String,
        customerInfo: Customer
    ) {

        Log.i(activity.javaClass.simpleName, "Firestore Add groupsId: ${groupsId}")

        mFireStore.collection(Constants.GROUPS)
            .document("${groupsId}")
            .collection(Constants.CUSTOMER)
            .add(customerInfo)
            .addOnSuccessListener {

                activity.customerUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "お客様情報追加エラー", e)
            }
    }

    fun getCustomerList(
        activity: EditCourseActivity,
        groupsId: String,
        dateSelect: String,
        courseSelect: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .whereEqualTo(Constants.DATE, dateSelect).whereEqualTo(Constants.COURSE, courseSelect)
            .orderBy(Constants.NO)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val customerList: ArrayList<Customer> = ArrayList()

                for (i in document.documents) {
                    val customer = i.toObject(Customer::class.java)!!
                    customer.customer_id = i.id
                    customerList.add(customer)
                }
                activity.successCustomerList(customerList)
                Log.i(javaClass.simpleName, "date : ${dateSelect} course : ${courseSelect}")
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "お客様リスト取り込みエラー"
                )
            }
    }

    fun getCustomerDetails(
        activity: DetailsCustomerActivity,
        groupsId: String,
        customerId: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .document(customerId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val customer = document.toObject(Customer::class.java)
                if (customer != null) {
                    activity.customerDetailsSuccess(customer)
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "お客様リスト取り込みエラー"
                )
            }
    }

    fun updateCustomerDetails(
        activity: EditCustomerActivity,
        groupsId: String,
        customerHashMap: HashMap<String, Any>,
        customerId: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .document(customerId)
            .update(customerHashMap)
            .addOnSuccessListener {
                activity.customerDetailsUpdateSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "お客様リスト更新エラー"
                )
            }
    }

    fun deleteCustomer(
        activity: DetailsCustomerActivity,
        groupsId: String,
        customerId: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .document(customerId)
            .delete()
            .addOnSuccessListener {
                activity.customerDeleteSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "お客様情報削除エラー", e)
            }
    }

    fun getCourseSetList(
        fragment: CustomerListFragment,
        groupsId: String,
        dateSelect: String,
        courseSelect: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .whereEqualTo(Constants.DATE, dateSelect)
            .whereEqualTo(Constants.COURSE, courseSelect)
            .orderBy(Constants.NO)
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())

                val customerList: ArrayList<Customer> = ArrayList()

                for (i in document.documents) {
                    val customer = i.toObject(Customer::class.java)!!
                    customer.customer_id = i.id
                    customerList.add(customer)
                }
                fragment.courseSetListUI(customerList)
                Log.i(
                    javaClass.simpleName,
                    "CustomerListFragment date : ${dateSelect} course : ${courseSelect}"
                )
            }

            .addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "お客様リスト取り込みエラー"
                )
            }
    }

    fun getMapsList(
        fragment: MapsFragment,
        groupsId: String,
        dateSelect: String,
        courseSelect: String
    ) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .whereEqualTo(Constants.DATE, dateSelect)
            .whereEqualTo(Constants.COURSE, courseSelect)
            .orderBy(Constants.NO)
            .get()
            .addOnSuccessListener { document ->
                Log.i(fragment.javaClass.simpleName, document.documents.toString())

                val mapsList: ArrayList<Customer> = ArrayList()

                for (i in document.documents) {
                    val customer = i.toObject(Customer::class.java)!!
                    customer.customer_id = i.id
                    mapsList.add(customer)
                }
                fragment.MapsListUI(mapsList)
                Log.i(
                    javaClass.simpleName,
                    "MapsFragment date : ${dateSelect} course : ${courseSelect}"
                )
            }

            .addOnFailureListener {
                fragment.hideProgressDialog()
                Log.e(
                    fragment.javaClass.simpleName,
                    "お客様リスト取り込みエラー"
                )
            }
    }

    fun getCustomerAll(activity: SolicitationMapsActivity, groupsId: String) {
        mFireStore.collection(Constants.GROUPS)
            .document(groupsId)
            .collection(Constants.CUSTOMER)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())

                val mapsList: ArrayList<Customer> = ArrayList()

                for (i in document.documents) {
                    val customer = i.toObject(Customer::class.java)!!
                    customer.customer_id = i.id
                    mapsList.add(customer)
                }
                activity.MapsListUI(mapsList)
                Log.i(
                    javaClass.simpleName,
                    "MapsFragment 読み込み完了"
                )
            }

            .addOnFailureListener {
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "お客様リスト取り込みエラー"
                )
            }
    }
}