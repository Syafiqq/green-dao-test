package com.github.syafiqq.greendaotest001.db.session

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import com.github.syafiqq.greendaotest001.entity.Note
import com.github.syafiqq.greendaotest001.entity.NoteDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.hamcrest.core.IsEqual
import org.hamcrest.core.IsNot
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionConsistencyTest {
    private var session: DaoSession? = null
    private var db: Database? = null
    private var helper: DaoMaster.DevOpenHelper? = null
    private var context: Context? = null
    private var dao: NoteDao? = null
    private var entity: Note? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        helper = DaoMaster.DevOpenHelper(context, "db-test")
        db = helper?.writableDb
        entity = Note().apply {
            text = "This is text"
            date = Date()
            user = null
        }

        assertThat(entity, IsNot(IsNull()))
        assertThat(context, IsNot(IsNull()))
        assertThat(helper, IsNot(IsNull()))
        assertThat(db, IsNot(IsNull()))
        assertThat(session, IsNull())
        assertThat(dao, IsNull())
    }

    @After
    fun tearDown() {
        dao?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test(expected = SQLiteConstraintException::class)
    fun it_should_forbid_using_insert() {
        session = DaoMaster(db).newSession(IdentityScopeType.None)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = Note().apply {
            id = 1L
            text = "New Text 2"
            date = Date()
            user = null
        }
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, IsNot(IsEqual(byId2)))
        assertThat(locId1, IsNot(IsEqual(locId2)))
        assertThat(byId1?.text, IsNot(IsEqual(byId2.text)))

        byId2.let { dao?.insert(it) }

        session?.clear()
        val byId11 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(byId2.id))
            ?.list()?.first()
        val locId11 = System.identityHashCode(byId11)
        assertThat(byId11, IsNot(IsEqual(byId2)))
        assertThat(locId11, IsNot(IsEqual(locId2)))
    }

    /**
     * Cache are easily changed if new object with same id operated
     * */
    @Test
    fun it_should_allow_using_update() {
        session = DaoMaster(db).newSession(IdentityScopeType.None)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = Note().apply {
            id = 1L
            text = "New Text 2"
            date = Date()
            user = null
        }
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, IsNot(IsEqual(byId2)))
        assertThat(locId1, IsNot(IsEqual(locId2)))
        assertThat(byId1?.text, IsNot(IsEqual(byId2.text)))

        byId2.let { dao?.update(it) }

        val byId21 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(byId2.id))
            ?.list()?.first()
        val locId21 = System.identityHashCode(byId21)

        assertThat(byId1, IsNot(IsEqual(byId2)))
        assertThat(locId1, IsNot(IsEqual(locId2)))
        assertThat(byId2, IsEqual(byId21))
        assertThat(locId2, IsEqual(locId21))
        assertThat(byId21, IsNot(IsEqual(byId1)))
        assertThat(locId21, IsNot(IsEqual(locId1)))

        byId1?.text = "New Text"
        byId1?.let { dao?.update(it) }

        val byId11 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(byId1?.id))
            ?.list()?.first()
        val locId11 = System.identityHashCode(byId11)

        assertThat(byId1, IsEqual(byId11))
        assertThat(locId1, IsEqual(locId11))
        assertThat(byId2, IsEqual(byId21))
        assertThat(locId2, IsEqual(locId21))
        assertThat(byId21, IsNot(IsEqual(byId11)))
        assertThat(locId21, IsNot(IsEqual(locId11)))

    }
}