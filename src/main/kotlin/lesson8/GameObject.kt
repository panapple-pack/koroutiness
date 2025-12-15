package lesson8

import kotlin.random.Random
import kotlin.random.nextInt

open class GameObject(
    // open - ключевое слово, которое позволяет наследовать данный класс другим классам

    var x: Double, // x - позиция по оси X (горизонталь - пока примитив)

    var speed: Double  // speed - скорость (сколько единиц в секунду объект пройдет по горизонтали)
) {
    open fun update(deltaTimeSeconds: Double) {
        // open fun позволяет переопределять метод в дочерних классах
        // без него override не будет работать
        // в роли параметра принимает кол-во секунд с прошлого кадра

        x += speed * deltaTimeSeconds
        // умножением мы указываем сколько единиц мы должны пройти за это время
        // Например: speed = 2.0 (2 юнита/секунду), delta = 0.5 сек
        // d = 2.0 * 0.5 = 1.0 (пройдет 1 юнит за отведенную дельту)
        // x +=  -  прибавляем посчитанное время к текущей позиции игрока или врага на координате x
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
        target.maxHealth -= damage
        println("Игрок нанес врагу $damage урона. Здоровье врага: ${target.maxHealth}")
        if (target.maxHealth <= 0) {
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
        target.maxHealth -= damage
        println("Враг нанес игроку $damage урона. Здоровье игрока: ${target.maxHealth}")
        if (target.maxHealth <= 0) {
            target.isAlive = false
            println("Игрок погиб")
        }
    }
}

