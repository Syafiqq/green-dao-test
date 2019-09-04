package com.github.syafiqq.greendaotest001.db.session

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.github.syafiqq.greendaotest001.entity.DaoMaster
import com.github.syafiqq.greendaotest001.entity.DaoSession
import com.github.syafiqq.greendaotest001.entity.Note
import com.github.syafiqq.greendaotest001.entity.NoteDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.identityscope.IdentityScopeType
import org.greenrobot.greendao.query.CloseableListIterator
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class SessionTest {
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
            this.text = "This is text"
            this.date = Date()
        }

        assertThat(entity, `is`(notNullValue()))
        assertThat(context, `is`(notNullValue()))
        assertThat(helper, `is`(notNullValue()))
        assertThat(db, `is`(notNullValue()))
        assertThat(session, `is`(nullValue()))
        assertThat(dao, `is`(nullValue()))
    }

    @After
    fun tearDown() {
        dao?.deleteAll()
        session?.clear()
        db?.close()
        helper?.close()
    }

    @Test
    fun it_should_return_same_location_id() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(equalTo(locId2)))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(equalTo(byId2?.text)))
    }

    @Test
    fun it_should_return_different_location_id() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId1 = System.identityHashCode(byId1)

        dao?.detachAll()

        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(not(equalTo(locId2))))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(not(equalTo(byId2?.text))))
    }

    @Test
    fun it_should_return_same_location_different_method() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.loadAll()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(equalTo(locId2)))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(equalTo(byId2?.text)))
    }

    @Test
    fun it_should_return_same_location_different_query() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.loadAll()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = dao?.queryBuilder()
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(equalTo(locId2)))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(equalTo(byId2?.text)))
    }

    @Test
    fun it_should_return_different_location_with_no_session() {
        session = DaoMaster(db).newSession(IdentityScopeType.None)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(not(equalTo(locId2))))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(not(equalTo(byId2?.text))))
    }

    @Test
    fun it_should_return_same_location_against_lazy_list() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        var lazy = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.listLazy()
        val byId1 = lazy?.first()
        lazy?.close()
        val locId1 = System.identityHashCode(byId1)

        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        lazy = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.listLazy()
        val byId3 = lazy?.first()
        lazy?.close()
        val locId3 = System.identityHashCode(byId3)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(equalTo(locId2)))
        assertThat(byId2, `is`(equalTo(byId3)))
        assertThat(locId2, `is`(equalTo(locId3)))
        assertThat(byId3, `is`(equalTo(byId1)))
        assertThat(locId3, `is`(equalTo(locId1)))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(equalTo(byId2?.text)))
    }

    @Test
    fun it_should_return_same_location_against_lazy_list_iterator() {
        session = DaoMaster(db).newSession(IdentityScopeType.Session)
        dao = session?.noteDao

        entity?.let { dao?.insert(it) }

        val byId1 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.listIterator()?.use(CloseableListIterator<Note>::next)
        val locId1 = System.identityHashCode(byId1)


        val byId2 = dao?.queryBuilder()
            ?.where(NoteDao.Properties.Id.eq(entity?.id))
            ?.list()?.first()
        val locId2 = System.identityHashCode(byId2)

        assertThat(byId1, `is`(equalTo(byId2)))
        assertThat(locId1, `is`(equalTo(locId2)))
        byId1?.text = "New Text"
        assertThat(byId1?.text, `is`(equalTo(byId2?.text)))
    }
}