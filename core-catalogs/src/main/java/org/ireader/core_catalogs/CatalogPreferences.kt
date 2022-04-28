

package org.ireader.core_catalogs

import org.ireader.core_api.prefs.Preference
import org.ireader.core_api.prefs.PreferenceStore
import javax.inject.Inject

class CatalogPreferences @Inject constructor(private val store: PreferenceStore) {

    fun lastRemoteCheck(): Preference<Long> {
        return store.getLong("last_remote_check", 0)
    }

    fun pinnedCatalogs(): Preference<Set<String>> {
        return store.getStringSet("pinned_catalogs", setOf())
    }
}
