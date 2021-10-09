package com.example.network

object Constants {


    const val LOGIN_URL = "api/2.0/authentication.json"

    const val TRY_PORTAL_URL = "api/2.0/portal"


    // Projects
    const val GET_PROJECTS_URL = "api/2.0/project/@self"

    //Milestones
    const val BASE_URL_MILESTONES_LATE = "api/2.0/project/milestone/"

    //Discussions
    const val BASE_URL_MESSAGES = "api/2.0/project/{projectid}/message"


    // Shared Preferences
    const val USER_TOKEN = "user_token"
    const val PORTAL_ADDRESS = "portal_address"
    const val AUTHENTICATED = "authenticated"
    const val EXPIRATION_DATE = "expirationDate"
    const val AUTH_TOKEN = "AuthToken"

}