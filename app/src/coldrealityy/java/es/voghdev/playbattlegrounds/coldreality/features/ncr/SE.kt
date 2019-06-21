package es.voghdev.playbattlegrounds.coldreality.features.ncr

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
