package com.github.syafiqq.greendaotest001.db

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class InstanceTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null

    @After
    fun tearDown() {
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_instantiate_context() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        assertThat(context, `is`(notNullValue()))
    }

    @Test
    fun it_should_instantiate_db_helper() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        assertThat(helper, `is`(notNullValue()))
    }

    @Test
    fun it_should_instantiate_writable_db() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        assertThat(db, `is`(notNullValue()))
    }

    @Test
    fun it_should_instantiate_dao_session() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        assertThat(session, `is`(notNullValue()))
    }
}