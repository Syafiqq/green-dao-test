package com.github.syafiqq.greendaotest001.entity

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Test

class NoteTest {
    @Test
    fun it_should_instantiate() {
        val entity = Note()
        assertThat(entity, `is`(notNullValue()))
    }

    @Test
    fun it_should_get_null_id() {
        val entity = Note()
        assertThat(entity.id, `is`(nullValue()))
    }
}