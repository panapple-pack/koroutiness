package lesson9

import kotlinx.coroutines.*

fun main() = runBlocking{
    // runBlocking нужен для запуска внутри нее вторичных процессов
    val player = GameCharacter(
        name = "Oleg",
        maxHealth = 50,
        maxMana = 30,
        baseAttack = 8,
        bonusAttack = 5
    )
    val enemy = GameCharacter(
        name = "Nikitos-peredoz",
        maxHealth = 50,
        maxMana = 0,
        baseAttack = 5,
        bonusAttack = 5
    )

    player.printStatus()
    enemy.printStatus()

    player.startManaRegeneration(
        amountPerTick = 3,
        intervalMillis = 3000L
    )

    println("${enemy.name} жестко токсит на ${player.name}")
    player.takeDamageByPoison(
        2,
        5,
        1500L
    )

    println("${player.name} получает бафф силы +5 на 5 секунд...")
    player.applyAttackBuff(5, 5000L)
    val attackJob = launch {
        repeat(6) {attackNumber ->
            // Повторить 6 раз лямбду (attackNumber считает всего попыток атаковать)
            delay(2000L)
            println("\nПопытка атаки #${attackNumber + 1}")
            player.attack(enemy)

            if (enemy.currentHealth <= 0) {
                println("враг повержен, останавливаем атаку")
                return@launch
                // return@launch - выходим из корутины attackJob
                // То есть для выхода (прерывания) корутины на данной точке нужно указать точку, откуда началась (launch)
            }
        }
    }
    var monitorJob = launch {

        repeat(12) {
            delay(1000L)

            println(">>> Мониторинг состояния персонажей <<<")
            player.printStatus()
            player.printStatus()

            if (player.currentHealth <= 0) {
                player.currentHealth = 0
                println("${player.name} помер")
                return@launch
            }
        }
    }
    delay(15000L)

    player.stopRegenerationMana()
    player.clearPotion()

    attackJob.cancel()
    monitorJob.cancel()



}