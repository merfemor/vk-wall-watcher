package com.merfemor.vkwallwatcher.telegram

import com.merfemor.vkwallwatcher.telegram.filter.PermittedUsersMessagesFilterImpl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.telegram.telegrambots.meta.api.objects.User

class PermittedUsersMessagesFilterImplTest {
    private val properties = mock<TelegramProperties>()
    private val underTest = PermittedUsersMessagesFilterImpl(properties)

    @Test
    fun `permit if property not specified`() {
        whenever(properties.permittedUsernames).doReturn(null)
        val permitted = underTest.test(createUserMock())
        Assertions.assertTrue(permitted)
    }

    @Test
    fun `permit if array is empty`() {
        whenever(properties.permittedUsernames).doReturn(emptyArray())
        val permitted = underTest.test(createUserMock())
        Assertions.assertTrue(permitted)
    }

    @Test
    fun `permit if in array`() {
        val name = "user"
        whenever(properties.permittedUsernames).doReturn(arrayOf(name))
        val permitted = underTest.test(createUserMock())
        Assertions.assertTrue(permitted)
    }

    @Test
    fun `reject if not in array`() {
        whenever(properties.permittedUsernames).doReturn(arrayOf("admin"))
        val permitted = underTest.test(createUserMock())
        println(properties.permittedUsernames)
        Assertions.assertFalse(permitted)
    }

    private companion object {
        private fun createUserMock(): User {
            return mock {
                on { userName } doReturn "user"
            }
        }
    }
}