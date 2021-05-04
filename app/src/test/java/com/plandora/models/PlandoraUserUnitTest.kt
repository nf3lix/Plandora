package com.plandora.models

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PlandoraUserUnitTest {

    private lateinit var user: PlandoraUser

    @Before
    fun createUser() {
        user = PlandoraUser("USER_ID", "USER_NAME",
            "DISPLAY_NAME", "USER_MAIL")
    }

    @Test
    fun getIdsFromUserObjects_isCorrect() {
        val userList : ArrayList<PlandoraUser> = arrayListOf(
            PlandoraUser(id = "ID_1"),
            PlandoraUser(id = "ID_2"),
            PlandoraUser(id = "ID_3")
        )
        val userIds = PlandoraUser().getIdsFromUserObjects(userList)
        for(i in 0 until userIds.size) {
            assertEquals(userList[i].id, userIds[i])
        }
    }

}