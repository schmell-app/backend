package no.schmell.backend.utils

fun shouldBeSwitchedOutWithName(questionString: String): Boolean {
    return (questionString.contains("N1") or questionString.contains("N2")
            or (questionString.contains("N3") or questionString.contains("N4")))
}

fun switchOutQuestionStringWithPlayers(questionString: String, playerArray: List<String>): String {
    if (!shouldBeSwitchedOutWithName(questionString)) return questionString

    val randomNumbers: MutableList<Int> = mutableListOf()

    for (i in 1..4) {
        var randomNumber = (playerArray.indices).random()
        while (randomNumbers.contains(randomNumber)) randomNumber = (playerArray.indices).random()
        randomNumbers.add(randomNumber)
    }

    return questionString
        .replace("N1", playerArray[randomNumbers[0]])
        .replace("N2", playerArray[randomNumbers[1]])
        .replace("N3", playerArray[randomNumbers[2]])
        .replace("N4", playerArray[randomNumbers[3]])

}