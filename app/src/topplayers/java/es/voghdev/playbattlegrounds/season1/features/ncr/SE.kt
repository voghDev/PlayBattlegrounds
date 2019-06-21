package es.voghdev.playbattlegrounds.season1.features.ncr

class SE {
    fun ncr(str: String): String {
        return str
            .mapIndexed { i, it ->
                if (i % 2 == 0)
                    it + 3
                else it + 5
            }
            .joinToString("")
    }

    fun dcr(str: String): String {
        return str
            .mapIndexed { i, it ->
                if (i % 2 == 0)
                    it - 3
                else it - 5
            }
            .joinToString("")
    }
}
