package lesson8

import Advanced_lesson7.Inventory
import lesson7.Item
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextInt

open class GameObject(
    // open - ключевое слово, которое позволяет наследовать данный класс другим классам

    var x: Double, // x - позиция по оси X (горизонталь - пока примитив)

    var speed: Double  // speed - скорость (сколько единиц в секунду объект пройдет по горизонтали)
) {
    var inventory: MutableList<Item> = mutableListOf()
    val items: MutableList<Item> = mutableListOf()

    open fun update(deltaTimeSeconds: Double, items: MutableList<Item>): Boolean {
        // open fun позволяет переопределять метод в дочерних классах
        // без него override не будет работать
        // в роли параметра принимает кол-во секунд с прошлого кадра

        x += speed * deltaTimeSeconds
        // умножением мы указываем сколько единиц мы должны пройти за это время
        // Например: speed = 2.0 (2 юнита/секунду), delta = 0.5 сек
        // d = 2.0 * 0.5 = 1.0 (пройдет 1 юнит за отведенную дельту)
        // x +=  -  прибавляем посчитанное время к текущей позиции игрока или врага на координате x

        if (Random.nextInt(100) <= 30) {
            if (inventory.size < 4) {
                val item = items[Random.nextInt(3)]
                inventory.add(item)
                println("В инвентарь добавлен ${item.name}")
            }
        }
        if (x >= 110) {
            return true
        }
        return false
    }
}

class Player(
    var isAlive: Boolean = true,
    x: Double,
    speed: Double,
    val name: String,
    var damage: Int = Random.nextInt(10, 41),
    var maxHealth: Int
) : GameObject(x, speed) {
    // GameObject означает наследовать родителя с передачей параметров базовому классу
    fun printPosition() {
        println("Игрок $name находится сейчас на позиции x = $x")
    }

    fun attack(target: Enemy) {
        if (!isAlive) return
        if (inventory.isNotEmpty()) {
            for (item in inventory) {
                if (item.id == 1) {
                    damage += 5
                    println("Урон по врагам увеличен на 5")
                }
                if (item.id == 2) {
                    maxHealth += 10
                    println("Здоровье игрока увеличено на 10")
                }
                if (item.id == 3) {
                    target.damage -= 3
                    println("Урон от врага уменьшен на 3")
                }
            }
        }
        target.maxHealth -= damage
        println("Игрок нанес врагу $damage урона. Здоровье врага: ${target.maxHealth}")
        if (target.maxHealth <= 0) {
            target.maxHealth = 0
            target.isAlive = false
            println("Игрок побеждает вражину")
        }
    }
}

class Enemy(
    var isAlive: Boolean = true,
    x: Double,
    speed: Double,
    val id: Int,
    var damage: Int = Random.nextInt(5, 21),
    var maxHealth: Int = Random.nextInt(40, 91)
) : GameObject(x, speed) {
    fun printPosition() {
        println("Враг $id находится сейчас на позиции x = $x")
    }

    fun attack(target: Player) {
        if (!isAlive) return
        target.maxHealth -= damage
        println("Враг нанес игроку $damage урона. Здоровье игрока: ${target.maxHealth}")
        if (target.maxHealth <= 0) {
            target.maxHealth = 0
            target.isAlive = false
            println("Игрок погиб")
        }
    }
}

