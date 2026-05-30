package com.example.data

data class DhikrItem(
    val id: Int,
    val category: String, // "MORNING", "EVENING", "AFTER_PRAYER", "SLEEP"
    val text: String,
    val countTarget: Int,
    val reward: String,
    val englishText: String = ""
)

object DhikrData {
    val ITEMS = listOf(
        // Morning Azkar
        DhikrItem(
            id = 1,
            category = "الأذكار الصباحية",
            text = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            countTarget = 1,
            reward = "من قالها حين يصبح حُفظ في يومه.",
            englishText = "We have entered a new day and with it all dominion belongs to Allah..."
        ),
        DhikrItem(
            id = 2,
            category = "الأذكار الصباحية",
            text = "أَعُوذُ بِكَلِمَاتِ اللهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
            countTarget = 3,
            reward = "لم تضره حشرة أو دابة أو عين في ليلته أو يومه.",
            englishText = "I seek refuge in the perfect words of Allah from the evil of what He has created."
        ),
        DhikrItem(
            id = 3,
            category = "الأذكار الصباحية",
            text = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
            countTarget = 3,
            reward = "لم يصبه فجأة بلاء وفي أمان وتيسير.",
            englishText = "In the name of Allah, with whose name nothing can cause harm in earth or heaven..."
        ),
        DhikrItem(
            id = 4,
            category = "الأذكار الصباحية",
            text = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ.",
            countTarget = 3,
            reward = "صلاح الأحوال واستجلاب الرحمة والتوفيق الإلهي الوفير.",
            englishText = "O Ever-Living, O Self-Sustaining, by Your mercy I seek help..."
        ),

        // Evening Azkar
        DhikrItem(
            id = 5,
            category = "الأذكار المسائية",
            text = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ لاَ إِلَهَ إِلاَّ اللَّهُ وَحْدَهُ لاَ شَرِيكَ لَهُ.",
            countTarget = 1,
            reward = "طُهارة النفس وحفظ الجسد طوال الليل.",
            englishText = "We have entered the evening and with it all dominion belongs to Allah..."
        ),
        DhikrItem(
            id = 6,
            category = "الأذكار المسائية",
            text = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ.",
            countTarget = 1,
            reward = "تجديد الإيمان وإقرار بملك الرحمان سبحانه.",
            englishText = "O Allah, by You we enter the evening, and by You we enter the morning..."
        ),
        DhikrItem(
            id = 7,
            category = "الأذكار المسائية",
            text = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ.",
            countTarget = 3,
            reward = "يُذهب الله بها الهم والغم ويقضي الدين العسير.",
            englishText = "O Allah, I seek refuge in You from anxiety and sorrow, weakness and laziness..."
        ),

        // Post-prayer Azkar
        DhikrItem(
            id = 8,
            category = "أذكار بعد الصلاة",
            text = "أَسْتَغْفِرُ اللهَ (ثلاثاً)، اللَّهُمَّ أَنْتَ السَّلَامُ وَمِنْكَ السَّلَامُ، تَبَارَكْتَ ذَا الْجَلَالِ وَالْإِكْرَامِ.",
            countTarget = 1,
            reward = "استغفار بعد الصلاة المفروضة مباشرة اتباعاً للسنة.",
            englishText = "I seek Allah's forgiveness (3 times). O Allah, You are peace..."
        ),
        DhikrItem(
            id = 9,
            category = "أذكار بعد الصلاة",
            text = "سُبْحَانَ اللهِ (33 مرة)، الْحَمْدُ لِلَّهِ (33 مرة)، اللهُ أَكْبَرُ (33 مرة).",
            countTarget = 33,
            reward = "تُغفر بها الذنوب والخطايا والزلات وإن كانت مثل زبد البحر.",
            englishText = "Glorified be Allah (33), Praise be to Allah (33), Allah is Greatest (33)"
        ),
        DhikrItem(
            id = 10,
            category = "أذكار بعد الصلاة",
            text = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
            countTarget = 1,
            reward = "تمام المائة بعد التسبيح والتحميد والتكبير بعد الفريضة.",
            englishText = "None has the right to be worshiped but Allah, alone without partner..."
        ),

        // Sleep Azkar
        DhikrItem(
            id = 11,
            category = "أذكار النوم",
            text = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي وَبِكَ أَرْفَعُهُ، إِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
            countTarget = 1,
            reward = "حفظ النفس والبدن من كيد الشياطين أثناء الغفلة والمنام.",
            englishText = "In Your name, my Lord, I lie down on my side, and by You I rise..."
        ),
        DhikrItem(
            id = 12,
            category = "أذكار النوم",
            text = "اللَّهُمَّ قِنِي عmanualَذَابَكَ يَوْمَ تَبْعَثُ عِبَادَكَ.",
            countTarget = 3,
            reward = "نجاة من هول عذاب الحشر والنار يوم الحساب.",
            englishText = "O Allah, save me from Your punishment on the Day You resurrect Your servants."
        )
    )
}
