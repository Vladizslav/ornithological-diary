package com.example.ornithologicaldiary.data.preferences

data class UserSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val sortOrder: SortOrder = SortOrder.DATE_DESC,
    val showNotes: Boolean = true
)

enum class ThemeMode(val label: String) {
    SYSTEM("Системная"),
    LIGHT("Светлая"),
    DARK("Тёмная")
}

enum class SortOrder(val label: String) {
    DATE_DESC("Сначала новые"),
    SPECIES_ASC("По названию вида")
}
