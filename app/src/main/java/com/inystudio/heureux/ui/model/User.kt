package com.inystudio.heureux.ui.model

class User {
    var profileUrl: String? = null
    var name: String? = null
    var phoneNumber: String? = null
    var nationalIdNumber: String? = null
    var residence: String? = null
    var purchases: String? = null //contains single or list of property id.

    constructor()     // Empty constructor needed for Firestore serialization

    constructor(profileUrl: String?, name: String?, phoneNumber: String?,
                nationalIdNumber: String?, residence: String?, purchases: String?) {
        this.profileUrl = profileUrl
        this.name = name
        this.phoneNumber = phoneNumber
        this.nationalIdNumber = nationalIdNumber
        this.residence = residence
        this.purchases = purchases
    }
}