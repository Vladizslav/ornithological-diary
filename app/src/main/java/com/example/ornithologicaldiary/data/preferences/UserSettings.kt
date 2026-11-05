package com.example.ornithologicaldiary.data.preferences

import androidx.annotation.StringRes
import com.example.ornithologicaldiary.R

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val showNotes: Boolean = true,
    val dailyReminder: Boolean = false
)

enum class ThemeMode(@StringRes val labelRes: Int) {
    SYSTEM(R.string.settings_theme_system),
    LIGHT(R.string.settings_theme_light),
    DARK(R.string.settings_theme_dark)
}

enum class SortOrder(@StringRes val labelRes: Int) {
    DATE_DESC(R.string.settings_sort_new_first),
    SPECIES_ASC(R.string.settings_sort_by_species)
}
