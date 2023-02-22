package designpatterns

import org.junit.Test
import java.io.File

/**
 * Observer pattern
 * define 1-to-many dependencies
 * when one object changes state, all of its dependencies are notified and updated
 * */

enum class EventType { OPEN, SAVED }
interface EventListener {
    fun update(eventType: EventType?, file: File?)
}

class EventManager(vararg operations: EventType) {

    var listeners = hashMapOf<EventType, ArrayList<EventListener>>()

    init {
        for (operation in operations) {
            listeners[operation] = ArrayList<EventListener>()
        }
    }

    fun subscribe(eventType: EventType?, listener: EventListener) {
        val users = listeners[eventType]
        users?.add(listener)
    }

    fun unsubscribe(eventType: EventType?, listener: EventListener) {
        val users = listeners[eventType]
        users?.remove(listener)
    }

    fun notify(eventType: EventType?, file: File) {
        val users = listeners[eventType]
        users?.let {
            for (listener in it) {
                listener.update(eventType, file)
            }
        }
    }
}

class Editor {

    var events: EventManager = EventManager(EventType.OPEN, EventType.SAVED)

    private var file: File? = null

    fun openFile(filePath: String) {
        file = File(filePath)
        events.notify(EventType.OPEN, file!!)
    }

    fun saveFile() {
        file?.let {
            events.notify(EventType.SAVED, file!!)
        }
    }
}

class EmailNotificationListener(private val email: String) : EventListener {
    override fun update(eventType: EventType?, file: File?) {
        println("Email to $email: Someone has performed $eventType operation with the file ${file?.name}")
    }
}

class LogOpenListener(private val fileName: String) : EventListener {
    override fun update(eventType: EventType?, file: File?) {
        println("Saved to $fileName: Someone has performed $eventType operation with the file ${file?.name}")
    }
}

class ObserverTest() {
    @Test
    fun testObserver() {
        val editor = Editor()
        editor.events.subscribe(EventType.OPEN, LogOpenListener("path/to/log/file.txt"))
        editor.events.subscribe(EventType.SAVED, EmailNotificationListener("test@test.com"))
        editor.openFile("test.txt")
        editor.saveFile()
    }
}
