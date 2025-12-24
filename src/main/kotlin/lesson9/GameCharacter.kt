package lesson9
// Корутины - это доступ к ассинхронному программированию (async - возможность выполнять больше 1 действия одновременно)
// Корутина - это выделенный поток, в котором и будет выполняться второстепенная/фоновая/дополнительная к основной части игры - процесс

import kotlinx.coroutines.*
import kotlinx.coroutines.time.delay

class GameCharacter(
    val name: String,
    val maxHealth: Int,
    val maxMana: Int,
    val baseAttack: Int,
    val bonusAttack: Int
) {
    var currentHealth: Int = maxHealth
    // Изменяемое здоровье, на которое будет накладываться модификатор (по умолчанию максимальное)

    var currentMana: Int = maxMana

    var canAttack: Boolean = true

    // Создание ссылки на корутину, чтобы можно было их вызывать или отменять(прерывать) по необходимости
    // Job? - "работа" корутины, на работу можно ссылаться, отменять, проверять ее выполнение и т.д. (Процесс)
    private var manaRegenJob: Job? = null
    //? - Доступность null в данной ссылке обязательна, т.к. ссылка по умолчанию может вовсе не ссылаться на корутины

    private var potionJob: Job? = null

    private var attackBonusJob: Job? = null
    // null - значит при создании ссылки она пока пустая
    // Корутина, которая будет обрабатывать получение урона от яда, и время сколько еще яд будет действовать

    fun isAlive(): Boolean {
        if (currentHealth <= 0) {
            currentHealth = 0
            return false
        }
        return true
    }

    fun printStatus() {
        println("=== Статус сущности $name ===")
        println("HP: $currentHealth/$maxHealth")
        println("MP: $currentMana/$maxMana")
        println("Может атаковать? $canAttack")
        println("------------------------------")
    }

    fun attack(target: GameCharacter) {
        if (!canAttack) {
            println("[!] $name не может пока атаковать")
            return
        }

        if (currentHealth <= 0) {
            println("[!] $name уже дед")
            return
        }

        if (attackBonusJob != null) {
            val damage = baseAttack + bonusAttack
            target.takeDamage(damage)
            println("$name атакует ${target.name} с дополнительным уроном и наносит $damage урона")
            return
        }

        val damage = baseAttack
        // К основному урону будем добавлять криты, бафы, эффекты и т.д.

        println("$name атакует ${target.name} и наносит $damage урона")
        target.takeDamage(damage)

        // Создаем кулдаун способности обычной атаки (2 сек после атаки задержка)
        startAttackCooldown(2000L)
    }

    fun takeDamage(amount: Int) {
        if(amount <= 0) {
            println("[!] $name не получает урона")
            return
        }

        currentHealth -= amount
        println("$name получил $amount урона. HP: $currentHealth/$maxHealth")

        if (currentHealth <= 0) {
            currentHealth = 0
            println("$name погиб")
        }
    }

    private fun startAttackCooldown(cooldownMillis: Long) {
        canAttack = false
        println("[*] $name получает кулдаун атаки на $cooldownMillis мс")

        // запуск корутины в глобальной области корутин
        GlobalScope.launch {
            // GlobalScope - глобальная область для корутин. Данная область выделяется и живет до тех пор, пока жив процесс,
            // в котором он объявлен. То есть запустили мы в глобальном потоке main - корутина прервется либо сама,
            // либо тогда, когда закончится main

            // launch - это запуск корутины в главном потоке (фоновая задача)

            delay(cooldownMillis)
            // delay - задержка. Ею мы задерживаем корутину (замораживаем, усыпляем)

            canAttack = true
            println("[*] кулдаун на атаку закончился")
        }
    }

    fun startManaRegeneration(
        amountPerTick: Int,  // Сколько маны восстановится на один тик
        intervalMillis: Long  // интервал между тем, как часто будет регенерироваться (вызываться 1 тик)
    ) {
        // Проверка, если мана уже регенерирует - перезапустить корутину
        if (!isAlive()) {
            println("$name умер и не может фармить ману")
            return
        }
        if (manaRegenJob != null) {
            println("[$] мана уже регенерирует, перезапускаем.......")
            manaRegenJob?.cancel()
            // cancel - прерывание корутины, на которую ссылаемся. Грубо говоря удаляет ее из фонового выполнения (не просто останавливает)
        }

        manaRegenJob = GlobalScope.launch {
            // В manaRegenJob сохраняем ссылку на этот фоновый процесс
            println("[*] Началась регенерация маны для $name")

            while (true) {
                // Бесконечный цикл - выйдем из него когда нам потребуется (когда восстановим ману целиком или умрем)
                delay(intervalMillis)
                // Ждем заданный интервал между каждой единицей восстановления маны

                if (currentHealth <= 0) {
                    println("[!] $name уже мертв")
                    break
                }
                if (currentMana >= maxMana) {
                    println("[*] Мана полностью восстановлена")
                    continue
                    // continue - переход к следующей итерации цикла
                }

                currentMana += amountPerTick

                if (currentMana > maxMana) {
                    currentMana = maxMana
                }
            }
            println("Корутина регенерации маны закончила работу")
        }
    }

    fun stopRegenerationMana() {
        if (manaRegenJob == null) {
            println("[!] Регенерация не начиналась, чтобы ее останавливать")
            return
        }
        manaRegenJob?.cancel()
        println("Регенерация маны завершена")
        manaRegenJob = null
    }

    fun takeDamageByPoison(ticks: Int, damagePerTick: Int, intervalMillis: Long) {
        if (manaRegenJob != null) {
            println("Яд уже действует на $name. Перезапускаем корутину")
            potionJob?.cancel()
        }
        potionJob = GlobalScope.launch {
            println("$name отравлен, эффект действует $ticks тиков")

            var remainingTicks = ticks
            // Сколько осталось тиков до завершения действия яда
            while (remainingTicks > 0) {
                delay(intervalMillis)

                if (currentHealth <= 0) {
                    currentHealth = 0
                    println("$name умер от отравления")
                    break
                }
                println("Яд наносит $damagePerTick урона $name")
                takeDamage(damagePerTick)
                remainingTicks -= 1
            }
            println("[^] Эффект действия яда на $name закончился")
        }
    }

    fun applyAttackBuff(bonus: Int, durationMillis: Long) {
        if (!isAlive()) {
            println("$name умер и не может использовать бонусные атаки")
            return
        }
        GlobalScope.launch {
            if (attackBonusJob != null) {
                println("Вы уже бафнулись, подождите")
                attackBonusJob?.cancel()
            }
            attackBonusJob = GlobalScope.launch {
                println("Наложение бонусного урона: $bonus")
                delay(durationMillis)

                attackBonusJob?.cancel()
                attackBonusJob = null
                println("Баф силы на $name закончился")
            }
        }
    }

    fun clearPotion() {
        if (potionJob == null) {
            println("[!] Эффект яда на $name не наложен")
            return
        }
        potionJob?.cancel()
        println("Эффект яда на $name очищен")
        potionJob = null
    }

    fun stopCoroutines() {
        potionJob?.cancel()
        potionJob = null
        attackBonusJob?.cancel()
        attackBonusJob = null
        manaRegenJob?.cancel()
        manaRegenJob = null
    }

    // Здесь после завершения корутины (или точнее выхода из launch{} - ссылка manaRegenJob станет null)
}