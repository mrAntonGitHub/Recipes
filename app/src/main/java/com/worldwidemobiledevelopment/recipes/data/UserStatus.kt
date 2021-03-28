package com.worldwidemobiledevelopment.recipes.data

enum class UserStatus(string: String, points: IntRange) {
    BEGINNER("Любитель", 0..100),
    ADEPT("Знаток", 100..300),
    EXPERT("Опытный", 300..600),
    SOUS_CHEF("Су-шеф", 600..1000),
    CHEF("Шеф-повар", 1000..1500),
    LEGEND("Легендарный повар", 1500..2000)
}