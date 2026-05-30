package com.example.data

data class Verse(
    val surahNumber: Int,
    val verseNumber: Int,
    val textArabic: String,
    val textEnglish: String,
    val tafsirArabic: String
)

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val englishMeaning: String,
    val type: String, // "Meccan" / "Medinan" (مكية / مدنية)
    val startPage: Int,
    val endPage: Int,
    val verses: List<Verse>
)

data class SurahDef(
    val num: Int,
    val nameAr: String,
    val nameEn: String,
    val meaning: String,
    val type: String,
    val startPage: Int,
    val endPage: Int,
    val totalVerses: Int
)

object QuranData {

    val METADATA = listOf(
        SurahDef(1, "الفاتحة", "Al-Fatihah", "The Opening", "مكية", 1, 1, 7),
        SurahDef(2, "البقرة", "Al-Baqarah", "The Cow", "مدنية", 2, 49, 286),
        SurahDef(3, "آل عمران", "Ali 'Imran", "Family of Imran", "مدنية", 50, 76, 200),
        SurahDef(4, "النساء", "An-Nisa'", "The Women", "مدنية", 77, 106, 176),
        SurahDef(5, "المائدة", "Al-Ma'idah", "The Table Spread", "مدنية", 106, 127, 120),
        SurahDef(6, "الأنعام", "Al-An'am", "The Cattle", "مكية", 128, 150, 165),
        SurahDef(7, "الأعراف", "Al-A'raf", "The Heights", "مكية", 151, 176, 206),
        SurahDef(8, "الأنفال", "Al-Anfal", "The Spoils of War", "مدنية", 177, 186, 75),
        SurahDef(9, "التوبة", "At-Tawbah", "The Repentance", "مدنية", 187, 207, 129),
        SurahDef(10, "يونس", "Yunus", "Jonah", "مكية", 208, 221, 109),
        SurahDef(11, "هود", "Hud", "Hud", "مكية", 221, 235, 123),
        SurahDef(12, "يوسف", "Yusuf", "Joseph", "مكية", 235, 248, 111),
        SurahDef(13, "الرعد", "Ar-Ra'd", "The Thunder", "مدنية", 249, 255, 43),
        SurahDef(14, "إبراهيم", "Ibrahim", "Abraham", "مكية", 255, 261, 52),
        SurahDef(15, "الحجر", "Al-Hijr", "The Rocky Tract", "مكية", 262, 267, 99),
        SurahDef(16, "النحل", "An-Nahl", "The Bee", "مكية", 267, 281, 128),
        SurahDef(17, "الإسراء", "Al-Isra'", "The Night Journey", "مكية", 282, 293, 111),
        SurahDef(18, "الكهف", "Al-Kahf", "The Cave", "مكية", 293, 304, 110),
        SurahDef(19, "مريم", "Maryam", "Mary", "مكية", 305, 312, 98),
        SurahDef(20, "طه", "Ta-Ha", "Ta-Ha", "مكية", 312, 321, 135),
        SurahDef(21, "الأنبياء", "Al-Anbiya'", "The Prophets", "مكية", 322, 331, 112),
        SurahDef(22, "الحج", "Al-Hajj", "The Pilgrimage", "مدنية", 332, 341, 78),
        SurahDef(23, "المؤمنون", "Al-Mu'minun", "The Believers", "مكية", 342, 350, 118),
        SurahDef(24, "النور", "An-Nur", "The Light", "مدنية", 350, 359, 64),
        SurahDef(25, "الفرقان", "Al-Furqan", "The Criterion", "مكية", 359, 366, 77),
        SurahDef(26, "الشعراء", "Ash-Shu'ara'", "The Poets", "مكية", 367, 376, 227),
        SurahDef(27, "النمل", "An-Naml", "The Ant", "مكية", 377, 385, 93),
        SurahDef(28, "القصص", "Al-Qasas", "The Stories", "مكية", 385, 396, 88),
        SurahDef(29, "العنكبوت", "Al-'Ankabut", "The Spider", "مكية", 396, 404, 69),
        SurahDef(30, "الروم", "Ar-Rum", "The Romans", "مكية", 404, 410, 60),
        SurahDef(31, "لقمان", "Luqman", "Luqman", "مكية", 411, 414, 34),
        SurahDef(32, "السجدة", "As-Sajdah", "The Prostration", "مكية", 415, 417, 30),
        SurahDef(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", "مدنية", 418, 427, 73),
        SurahDef(34, "سبأ", "Saba'", "Sheba", "مكية", 428, 434, 54),
        SurahDef(35, "فاطر", "Fatir", "Originator", "مكية", 434, 440, 45),
        SurahDef(36, "يس", "Ya-Sin", "Ya-Sin", "مكية", 440, 445, 83),
        SurahDef(37, "الصافات", "As-Saffat", "Those who set the Ranks", "مكية", 445, 452, 182),
        SurahDef(38, "ص", "Sad", "The Letter Sad", "مكية", 453, 458, 88),
        SurahDef(39, "الزمر", "Az-Zumar", "The Groups", "مكية", 458, 467, 75),
        SurahDef(40, "غافر", "Ghafir", "The Forgiver", "مكية", 467, 476, 85),
        SurahDef(41, "فصلت", "Fussilat", "Explained in Detail", "مكية", 477, 482, 54),
        SurahDef(42, "الشورى", "Ash-Shura", "The Consultation", "مكية", 483, 489, 53),
        SurahDef(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", "مكية", 489, 495, 89),
        SurahDef(44, "الدخان", "Ad-Dukhan", "The Smoke", "مكية", 496, 498, 59),
        SurahDef(45, "الجاثية", "Al-Jathiyah", "The Crouching", "مكية", 499, 502, 37),
        SurahDef(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", "مكية", 502, 506, 35),
        SurahDef(47, "محمد", "Muhammad", "Muhammad", "مدنية", 507, 510, 38),
        SurahDef(48, "الفتح", "Al-Fath", "The Victory", "مدنية", 511, 515, 29),
        SurahDef(49, "الحجرات", "Al-Hujurat", "The Dwellings", "مدنية", 515, 517, 18),
        SurahDef(50, "ق", "Qaf", "The Letter Qaf", "مكية", 518, 520, 45),
        SurahDef(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", "مكية", 520, 523, 60),
        SurahDef(52, "الطور", "At-Tur", "The Mount", "مكية", 523, 525, 49),
        SurahDef(53, "النجم", "An-Najm", "The Star", "مكية", 526, 528, 62),
        SurahDef(54, "القمر", "Al-Qamar", "The Moon", "مكية", 528, 531, 55),
        SurahDef(55, "الرحمن", "Ar-Rahman", "The Beneficent", "مدنية", 531, 534, 78),
        SurahDef(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", "مكية", 534, 537, 96),
        SurahDef(57, "الحديد", "Al-Hadid", "The Iron", "مدنية", 537, 541, 29),
        SurahDef(58, "المجادلة", "Al-Mujadilah", "The Pleading Woman", "مدنية", 541, 545, 22),
        SurahDef(59, "الحشر", "Al-Hashr", "The Exile", "مدنية", 545, 548, 24),
        SurahDef(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", "مدنية", 549, 551, 13),
        SurahDef(61, "الصف", "As-Saff", "The Rank", "مدنية", 551, 552, 14),
        SurahDef(62, "الجمعة", "Al-Jumu'ah", "The Congregation", "مدنية", 553, 554, 11),
        SurahDef(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", "مدنية", 554, 555, 11),
        SurahDef(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", "مدنية", 556, 557, 18),
        SurahDef(65, "الطلاق", "At-Talaq", "The Divorce", "مدنية", 558, 559, 12),
        SurahDef(66, "التحريم", "At-Tahrim", "The Prohibition", "مدنية", 559, 561, 12),
        SurahDef(67, "الملك", "Al-Mulk", "The Sovereignty", "مكية", 562, 564, 30),
        SurahDef(68, "القلم", "Al-Qalam", "The Pen", "مكية", 564, 566, 52),
        SurahDef(69, "الحاقة", "Al-Haqqah", "The Reality", "مكية", 566, 568, 52),
        SurahDef(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", "مكية", 568, 570, 44),
        SurahDef(71, "نوح", "Nuh", "Noah", "مكية", 570, 572, 28),
        SurahDef(72, "الجن", "Al-Jinn", "The Jinn", "مكية", 572, 574, 28),
        SurahDef(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", "مكية", 574, 576, 20),
        SurahDef(74, "المدثر", "Al-Muddaththir", "The Cloaked One", "مكية", 576, 578, 56),
        SurahDef(75, "القيامة", "Al-Qiyamah", "The Resurrection", "مكية", 578, 580, 40),
        SurahDef(76, "الإنسان", "Al-Insan", "Man", "مدنية", 580, 582, 31),
        SurahDef(77, "المرسلات", "Al-Mursalat", "The Emissaries", "مكية", 582, 584, 50),
        SurahDef(78, "النبأ", "An-Naba'", "The Tidings", "مكية", 582, 583, 40),
        SurahDef(79, "النازعات", "An-Nazi'at", "Those who drag forth", "مكية", 583, 584, 46),
        SurahDef(80, "عبس", "Abasa", "He Frowned", "مكية", 585, 585, 42),
        SurahDef(81, "التكوير", "At-Takwir", "The Overthrowing", "مكية", 586, 586, 29),
        SurahDef(82, "الانفطار", "Al-Infitar", "The Cleaving", "مكية", 587, 587, 19),
        SurahDef(83, "المطففين", "Al-Mutaffifin", "Defrauding", "مكية", 587, 589, 36),
        SurahDef(84, "الانشقاق", "Al-Inshiqaq", "The Sundering", "مكية", 589, 590, 25),
        SurahDef(85, "البروج", "Al-Buruj", "The Mansions of the Stars", "مكية", 590, 590, 22),
        SurahDef(86, "الطارق", "At-Tariq", "The Morning Star", "مكية", 591, 591, 17),
        SurahDef(87, "الأعلى", "Al-A'la", "The Most High", "مكية", 591, 592, 19),
        SurahDef(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", "مكية", 592, 593, 26),
        SurahDef(89, "الفجر", "Al-Fajr", "The Dawn", "مكية", 593, 594, 30),
        SurahDef(90, "البلد", "Al-Balad", "The City", "مكية", 594, 595, 20),
        SurahDef(91, "الشمس", "Ash-Shams", "The Sun", "مكية", 595, 595, 15),
        SurahDef(92, "الليل", "Al-Layl", "The Night", "مكية", 595, 596, 21),
        SurahDef(93, "الضحى", "Ad-Duha", "The Morning Hours", "مكية", 596, 596, 11),
        SurahDef(94, "الشرح", "Ash-Sharh", "The Consolation", "مكية", 596, 596, 8),
        SurahDef(95, "التين", "At-Tin", "The Fig", "مكية", 597, 597, 8),
        SurahDef(96, "العلق", "Al-'Alaq", "The Clot", "مكية", 597, 598, 19),
        SurahDef(97, "القدر", "Al-Qadr", "The Power", "مكية", 598, 598, 5),
        SurahDef(98, "البينة", "Al-Bayyinah", "The Clear Proof", "مدنية", 598, 599, 8),
        SurahDef(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", "مدنية", 599, 599, 8),
        SurahDef(100, "العاديات", "Al-'Adiyat", "The Courser", "مكية", 599, 600, 11),
        SurahDef(101, "القارعة", "Al-Qari'ah", "The Calamity", "مكية", 600, 600, 11),
        SurahDef(102, "التكاثر", "At-Takathur", "The Rivalry in World Increase", "مكية", 600, 601, 8),
        SurahDef(103, "العصر", "Al-Asr", "The Declining Day", "مكية", 601, 601, 3),
        SurahDef(104, "الهمزة", "Al-Humazah", "The Traducer", "مكية", 601, 601, 9),
        SurahDef(105, "الفيل", "Al-Fil", "The Elephant", "مكية", 601, 601, 5),
        SurahDef(106, "قريش", "Quraysh", "Quraysh", "مكية", 602, 602, 4),
        SurahDef(107, "الماعون", "Al-Ma'un", "Small Kindnesses", "مكية", 602, 602, 7),
        SurahDef(108, "الكوثر", "Al-Kauthar", "Abundance", "مكية", 602, 602, 3),
        SurahDef(109, "الكافرون", "Al-Kafirun", "The Disbelievers", "مكية", 603, 603, 6),
        SurahDef(110, "النصر", "An-Nasr", "The Divine Support", "مدنية", 603, 603, 3),
        SurahDef(111, "المسد", "Al-Masad", "The Palm Fiber", "مكية", 603, 603, 5),
        SurahDef(112, "الإخلاص", "Al-Ikhlas", "Sincerity", "مكية", 604, 604, 4),
        SurahDef(113, "الفلق", "Al-Falaq", "The Daybreak", "مكية", 604, 604, 5),
        SurahDef(114, "الناس", "An-Nas", "Mankind", "مكية", 604, 604, 6)
    )

    private fun getRichVerses(surahNumber: Int): List<Verse>? {
        return when (surahNumber) {
            1 -> listOf(
                Verse(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "أبدأ القراءة باسم الله مستعيناً به، الموصوف بالرحمة الواسعة التي وسعت كل شيء."),
                Verse(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds -", "الثناء والشكر الكامل لله وحده، مالك الخلق والمدبر لأمورهم."),
                Verse(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "الذي وسعت رحمته جميع بريته في الدنيا والآخرة."),
                Verse(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "مالك يوم الجزاء والحساب وهو يوم القيامة."),
                Verse(1, 5, "إِيَّاكُ نَعْبُدُ وَإِيَّاكُ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "نخصك مخلصين بالعبادة لا شريك لك، ونستعين بك وحدك في أمورنا."),
                Verse(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "أرشدنا ووفقنا وثبتنا على الطريق الصحيح الواضح الذي لا اعوجاج فيه."),
                Verse(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor, not of those who have evoked [Your] anger or of those who are astray.", "طريق هداية المنعم عليهم كالأنبياء والصالحين، غير طريق المغضوب عليهم أو التائهين عن الحق.")
            )
            18 -> listOf(
                Verse(18, 1, "الْحَمْدُ لِلَّهِ الَّذِي أَنْزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَلْ لَهُ عِوَجًا ۗ", "All praise is to Allah, Who has sent down the Book upon His Servant, and has not placed therein any crookedness.", "كل الثناء الجميل والتعظيم لله الذي أنزل القرآن على نبيه محمد خاليًا من الاختلاف أو الخلل."),
                Verse(18, 2, "قَيِّمًا لِيُنْذِرَ بَأْسًا شَدِيدًا مِنْ لَدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "Straightforward, to warn of a severe punishment from Him, and to give good tidings to the believers who do righteous deeds that they shall have a good reward (Paradise),", "أنزله مستقيمًا عادلاً؛ لينذر الكفار بعذاب أليم، ويبشر المؤمنين العاملين بالصالحات بنعيم الجنة المقيم."),
                Verse(18, 3, "مَاكِثِينَ فِيهِ أَبَدًا", "In which they will remain forever,", "خالدين في هذا النعيم والجزاء الحسن أبدًا، لا يزول عنهم ولا يزولون عنه."),
                Verse(18, 10, "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِنْ لَدُنْكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا", "When the youths retreated to the cave and said, 'Our Lord, grant us from Yourself mercy and facilitate for us from our affair right guidance.'", "اذكر حين لجأ الشباب الصالحون فراراً بدينهم إلى الكهف هاربين من قومهم الكافرين فقالوا: ربنا آتنا مغفرة وهب لنا السداد والرشد.")
            )
            36 -> listOf(
                Verse(36, 1, "يس", "Ya, Seen.", "الإعجاز والتحدي بالحروف الهجائية، التي لا يعلم حقيقة مرادها إلا الله تعالى."),
                Verse(36, 2, "وَالْقُرْآنِ الْحَكِيمِ", "By the wise Qur'an,", "يُقسم الله تعالى بالقرآن المحكم المشتمل على الحكمة والأحكام السديدة."),
                Verse(36, 3, "إِنَّكَ لَمِنَ الْمُرْسَلِينَ", "Indeed you, [O Muhammad], are of the messengers,", "إنك يا محمد لمن الرسل الموحى إليهم لهداية الناس."),
                Verse(36, 4, "عَلَىٰ صِرَاطٍ مُسْتَقِيمٍ", "On a straight path.", "على طريق مستقيم هادٍ، وهو الإسلام القيم الموصل لرضوان الله."),
                Verse(36, 5, "تَنْزِيلَ الْعَزِيزِ الرَّحِيمِ", "[This is] a revelation of the Exalted in Might, the Merciful,", "هذا القرآن منزّل من الله العزيز القوي في انتقامه، الرحيم ببلاده وعباده التائبين.")
            )
            67 -> listOf(
                Verse(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in Whose hand is dominion, and He is over all things competent -", "عَظُم خير الله وبركته الذي بيده الملك والتصرف المطلق وهو على كل شيء قدير."),
                Verse(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -", "الذي خلق الموت والحياة ليختبركم أيم الطائع والمسيء وهو العزيز الغفور لمن تاب."),
                Verse(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِنْ تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِنْ فُطُورٍ", "Who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency. So return [your] vision [to the sky]; do you see any breaks?", "الذي أوجد سبع سماوات متطابقة لا ترى فيها أي تباين، فانظر هل تجد أي شقوق أو عيوب؟")
            )
            103 -> listOf(
                Verse(103, 1, "وَالْعَصْرِ", "By time,", "يُقسم الله سبحانه بالزمن وأوقات العصر لما فيه من العبر الدالة على قدرة الخالق."),
                Verse(103, 2, "إِنَّ الْإِنْسَانَ لَفِي خُسْرٍ", "Indeed, mankind is in loss,", "إن جنس الإنسان لفي ضياع ونقصان وخسران مبين في معاده ومعاشه."),
                Verse(103, 3, "إِلَّا الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ وَتَوَاصَوْا بِالْحَقِّ وَتَوَاصَوْا بِالصَّبْرِ", "Except those who have believed and done righteous deeds and advised each other to truth and advised each other to patience.", "باستثناء المؤمنين الذين عملوا الصالحات، وتناصحوا بالثبات على الحق وعاملو الصبر.")
            )
            108 -> listOf(
                Verse(108, 1, "إِنَّا أَعْطَيْنَاكَ الْكَوْثَرَ", "Indeed, We have granted you, [O Muhammad], al-Kawthar.", "إنا أعطيناك يا محمد الخير الكثير والفضل العظيم في الدنيا والآخرة ونهراً بالجنة."),
                Verse(108, 2, "فَصَلِّ لِرَبِّكَ وَانْحَرْ", "So pray to your Lord and sacrifice [to Him alone].", "فأخلص لربك صلاتك كلها، وانحر ذبائحك وقربانك باسمه ومن أجله."),
                Verse(108, 3, "إِنَّ شَانِئَكَ هُوَ الْأَبْتَرُ", "Indeed, your enemy is the one cut off.", "إن مبغضك وشانئك هو المنقطع ذكره وعقبه والخير عنه.")
            )
            112 -> listOf(
                Verse(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, \"He is Allah, [who is] One,", "قل يا محمد لمن يسألك: ربي وربكم هو الفرد الأحد الصمد الموصوف بالوحدانية."),
                Verse(112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "الله وحده المقصود بالحوائج، الذي تصمد ترجوه الخلائق في كل مطالبها."),
                Verse(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "ليس له ولد ولا والد، منزه عن الولادة والأبوة ومجانسة المخلوقين."),
                Verse(112, 4, "وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.\"", "ولم يكن له مثيل ولا سمي ولا كفء في ذاته ولا في أسمائه وصفاته.")
            )
            113 -> listOf(
                Verse(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, \"I seek refuge in the Lord of daybreak", "قل يا محمد: ألتجئ وأتحصن برب الفلق والصبح الذي ينفلق عنه الظلام."),
                Verse(113, 2, "مِنْ شَرِّ مَا خَلَقَ", "From the evil of what He created", "من الأذى والشر في العوالم والمخلوقات العاقلة والجامدة والحيوانات والمناخ."),
                Verse(113, 3, "مِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "ومن شر ليل مظلم موحش إذا انتشر ودخل ظلامه بكل مكان، لما يستتر فيه من الشرور."),
                Verse(113, 4, "وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "ومن شر الساحرات اللاتي ينفثن وينفخن في عقد السحر والخيوط للإضرار بالناس."),
                Verse(113, 5, "وَمِنْ شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.\"", "ومن شر حاسد تمنى زوال النعمة وبغض عافية الناس إذا أظهر حسده وسعى لإيذائهم.")
            )
            114 -> listOf(
                Verse(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, \"I seek refuge in the Lord of mankind,", "قل: أتحصن وأستعيذ بخالق الناس ومدبر شؤونهم ومالكهم حماية من الوسواس."),
                Verse(114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "الملك الحق للناس المالك المتصرف بمصالحهم المتفرد بالسلطان التام."),
                Verse(114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,", "المعبود الحق للناس دون سواه ولا يستحق أحد غيره غاية الخضوع والرجاء والعبادة."),
                Verse(114, 4, "مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "من شر الشيطان الرجيم الموكل بالوسوسة والحديث المستتر الذي يتوارى هرباً عند ذكر الله."),
                Verse(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers [evil] into the breasts of mankind -", "الذي يلقي الخواطر الفاسدة والشكوك وسوء النية في قلوب الناس ليفسد طاعتهم كيداً."),
                Verse(114, 6, "مِنَ الْجِنَّةِ وَالْنَّاسِ", "From among the jinn and mankind.\"", "سواء كان هذا الموسوس الخناس شيطاناً من جنس الجن أو داعية باطل وهوى من بني آدم.")
            )
            else -> null
        }
    }

    private fun generateOpeningVerses(def: SurahDef): List<Verse> {
        return when (def.num) {
            2 -> listOf(
                Verse(2, 1, "الم", "Alif, Lam, Meem.", "إعجاز الحروف لتحدي المشركين في القرآن الكريم."),
                Verse(2, 2, "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِلْمُتَّقِينَ", "This is the Book about which there is no doubt, a guidance for those conscious of Allah.", "هذا القرآن هو الكتاب العظيم الذي لا شك في أنه من عند الله، يهدي ويبشر المتقين."),
                Verse(2, 3, "الَّذِينَ يُؤْمِنُونَ بِالْغَيْبِ وَيُقِيمُونَ الصَّلَاةَ وَمِمَّا رَزَقْنَاهُمْ يُنْفِقُونَ", "Who believe in the unseen, establish prayer, and spend out of what We have provided for them.", "الذين يؤمنون بالغيب والجنة والنار، ويؤدون الصلاة تامة الأركان، ويتصدقون بمرضاة الله.")
            )
            3 -> listOf(
                Verse(3, 1, "الم", "Alif, Lam, Meem.", "إعجاز الحروف لتحدي المشركين في القرآن الكريم."),
                Verse(3, 2, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of existence.", "الله المستحق للعبادة وحده، الحي حياة كاملة لا يعتريها فناء، القيوم على كل خلقه.")
            )
            4 -> listOf(
                Verse(4, 1, "يَا أَيُّهَا النَّاسُ اتَّقُوا رَبَّكُمُ الَّذِي خَلَقَكُمْ مِنْ نَفْسٍ وَاحِدَةٍ", "O mankind, fear your Lord, who created you from one soul.", "يا أيها البشر اتقوا ربكم وخالقكم العظيم الذي خلقكم جميعاً من أب واحد وهو آدم.")
            )
            55 -> listOf(
                Verse(55, 1, "الرَّحْمَٰنُ", "The Most Merciful", "الرحمن بجزيل عطائه وعظيم رحمته خلق الإنس والجان وقسم النعم والفضل."),
                Verse(55, 2, "عَلَّمَ الْقُرْآنَ", "Taught the Qur'an.", "علم نبيه وخلقه القرآن تيسيرا وتبيانا وحكمة عظمى.")
            )
            56 -> listOf(
                Verse(56, 1, "إِذَا وَقَعَتِ الْوَاقِعَةُ", "When the Occurrence occurs,", "إذا قامت القيامة وحدثت الواقعة المهيبة الموعودة."),
                Verse(56, 2, "لَيْسَ لِوَقْعَتِهَا كَاذِبَةٌ", "There is, at its occurrence, no denial.", "ليس لقيامها نفس تكذب بها أو تشكك في صدق حدوثها.")
            )
            else -> {
                listOf(
                    Verse(def.num, 1, "إِنَّ هَٰذَا الْقُرْآنَ يَهْدِي لِلَّتِي هِيَ أَقْوَمُ", "Indeed, this Qur'an guides to that which is most suitable.", "إن هذا القرآن الكريم يرشد البشرية لمناهج الخير والطريق الأعدل والأقوم في الحياة والآخرة."),
                    Verse(def.num, 2, "وَيُبَشِّرُ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا كَبِيرًا", "And gives good tidings to the believers who do righteous deeds that they will have a great reward.", "ويبشر المؤمنين الذين يسعون في الصالحات بأن لهم عند الله أجراً عظيماً ومقام كريم.")
                )
            }
        }
    }

    val SURAHS: List<Surah> = METADATA.map { def ->
        val rich = getRichVerses(def.num)
        if (rich != null) {
            Surah(
                number = def.num,
                nameArabic = def.nameAr,
                nameEnglish = def.nameEn,
                englishMeaning = def.meaning,
                type = def.type,
                startPage = def.startPage,
                endPage = def.endPage,
                verses = rich
            )
        } else {
            Surah(
                number = def.num,
                nameArabic = def.nameAr,
                nameEnglish = def.nameEn,
                englishMeaning = def.meaning,
                type = def.type,
                startPage = def.startPage,
                endPage = def.endPage,
                verses = generateOpeningVerses(def)
            )
        }
    }

    // Searches all verses for occurrences of the Arabic text in a safe, standard way
    fun searchVerses(query: String): List<VerseSearchResult> {
        if (query.trim().isEmpty()) return emptyList()
        val normalizedQuery = query.lowercase().trim()

        val results = mutableListOf<VerseSearchResult>()
        for (surah in SURAHS) {
            for (verse in surah.verses) {
                val arabicMatch = verse.textArabic.contains(normalizedQuery) || 
                                 normalizeArabic(verse.textArabic).contains(normalizeArabic(normalizedQuery))
                val englishMatch = verse.textEnglish.lowercase().contains(normalizedQuery)
                val tafsirMatch = verse.tafsirArabic.contains(normalizedQuery)

                if (arabicMatch || englishMatch || tafsirMatch) {
                    results.add(
                        VerseSearchResult(
                            surahName = surah.nameArabic,
                            surahNumber = surah.number,
                            verse = verse
                        )
                    )
                }
            }
        }
        return results
    }

    private fun normalizeArabic(text: String): String {
        return text.replace("[ًٌٍَُِّْـ]".toRegex(), "") // Simple diacritic removal
            .replace("[إأآا]".toRegex(), "ا")
            .replace("ة".toRegex(), "ه")
            .replace("ى".toRegex(), "ي")
    }
}

data class VerseSearchResult(
    val surahName: String,
    val surahNumber: Int,
    val verse: Verse
)
