package taskmanager;

import task.Task;
import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Node head = null;
    private Node tail = null;



    private final Map<Integer, Node> idAndTaskNode = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            int taskID = task.getTaskID();
            if (idAndTaskNode.containsKey(taskID)) {
                remove(taskID);
            }
            Node newNode = new Node(task);

            if (head == null) {
                head = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
            }
            tail = newNode;
            idAndTaskNode.put(taskID, newNode);
        }
    }

    @Override
    public void remove(int taskID) {
        if (idAndTaskNode.containsKey(taskID)) {
            Node oldNode = idAndTaskNode.get(taskID);
            if (oldNode == head) {
                head = head.next;
            } else if (oldNode == tail) {
                tail.prev.next = null;
                tail = tail.prev;
            } else {
                oldNode.prev.next = oldNode.next;
                oldNode.next.prev = oldNode.prev;
            }
            idAndTaskNode.remove(taskID);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> calledTasks = new ArrayList<>();
        if (head == null) {
            return calledTasks;
        }
        Node currentNode = head;

        while (currentNode != null) {
            calledTasks.add(currentNode.task);
            currentNode = currentNode.next;
        }
        return calledTasks;
    }

    private static class Node {
        Task task;
        Node prev;
        Node next;

        Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }


}
