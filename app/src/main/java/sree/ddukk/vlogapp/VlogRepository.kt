package sree.ddukk.vlogapp

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VlogRepository @Inject constructor(
    private val dao: VlogDao
) {
    val allVlogs: Flow<List<VlogEntry>> = dao.getAllVlogs()

    suspend fun insertVlog(vlog: VlogEntry) {
        dao.insertVlog(vlog)
    }
    suspend fun deleteVlog(vlog: VlogEntry) {
        dao.delete(vlog)
    }

}
