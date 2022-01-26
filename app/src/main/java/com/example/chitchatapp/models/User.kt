package com.example.chitchatapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//Parcelize to pass User from one activity to other
@Parcelize
class User(val uid: String, val userName: String, val profileImageUrl: String): Parcelable {
    constructor() : this("", "", "")
}