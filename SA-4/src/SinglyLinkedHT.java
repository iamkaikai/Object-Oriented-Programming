/**
 * A singly-linked list implementation of the SimpleList interface
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012, 
 * based on a more feature-ful version by Tom Cormen and Scot Drysdale
 * @author CBK, Spring 2016, cleaned up inner class
 * @author Tim Pierson, Spring 2017
 * @author Tim Pierson, Spring 2023, provided add method with no index (adds at end)
 */
public class SinglyLinkedHT<T> implements SimpleList<T> {
	private Element head;	// front of the linked list
	private Element tail;	// end
	private int size;		// # elements in the list

	/**
	 * The linked elements in the list: each has a piece of data and a next pointer
	 */
	private class Element {
		private T data;
		private Element next;

		private Element(T data, Element next) {
			this.data = data;
			this.next = next;
		}
	}

	public SinglyLinkedHT() {
		// TODO: this is just copied from SinglyLinked; modify as needed
		head = null;
		tail = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	/**
	 * Helper function, advancing to the nth Element in the list and returning it
	 * (exception if not that many elements)
	 */
	private Element advance(int n) throws Exception {
		Element e = head;
		while (n > 0) {
			// Just follow the next pointers
			e = e.next;
			if (e == null) throw new Exception("invalid index");
			n--;
		}
		return e;
	}

	public void add(int idx, T item) throws Exception {        
		// TODO: this is just copied from SinglyLinked; modify as needed
		if (idx < 0 || idx > size+1) {
			throw new Exception("invalid index!");
		}else if(idx == 0){
			head = new Element(item, head);
			// if the list is empty, assign head as tail
			if(tail == null){
				tail = head;
			}
		}else {
			Element e = advance(idx-1);
			e.next = new Element(item, e.next);		
			if(idx == size){
				tail = e.next;
			}
		}
		size++;
	}

	/**
	 * Add item at end of List
	 * @param item - item to add to List
	 * @throws Exception
	 */
	public void add(T item) throws Exception {
		//TODO: complete this method efficiently using tail pointer
		if(head == null){
			Element e = new Element(item, null);
			head = e;
			tail = e;
		}else{
			tail.next = new Element(item, tail.next);
			tail = tail.next;	
		}
	}

	public void remove(int idx) throws Exception {
		// TODO: this is just copied from SinglyLinked; modify as needed
		if (idx == 0) {
			// Just pop off the head
			if (head == null) throw new Exception("invalid index");
			head = head.next;
			if(head.next == null){
				tail = null;
			}
		} else {
			// It's the next thing after element # idx-1
			Element e = advance(idx-1);
			if (e.next == null) throw new Exception("invalid index");
			// Splice it out
			if(e.next.next == null){
				tail = e;
			}
			e.next = e.next.next;
		}
		size--;
	}

	public T get(int idx) throws Exception {
		Element e = advance(idx);
		return e.data;
	}

	public void set(int idx, T item) throws Exception {
		Element e = advance(idx);
		e.data = item;
	}

	/**
	 * Appends other to the end of this in constant time, by manipulating head/tail pointers
	 * @throws Exception
	 */
	public void append(SinglyLinkedHT<T> other) throws Exception {
		// TODO: YOUR CODE HERE
		if(other.head == null){
			return;
		}else if(head == null){
			head = other.head;
			tail = other.tail;
			size = other.size;
		}else{
			tail.next = other.head;
			tail = other.tail;
			size += other.size;
		}
	}
	
	public String toString() {
		String result = "";
		for (Element x = head; x != null; x = x.next) 
			result += x.data + "->"; 
		result += "[/]";

		return result;
	}

	public static void main(String[] args) throws Exception {
		SinglyLinkedHT<String> list1 = new SinglyLinkedHT<String>();
		SinglyLinkedHT<String> list2 = new SinglyLinkedHT<String>();
		
		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1);

		list2.add(0, "a");
		System.out.println(list1 + " + " + list2);
		list1.append(list2);
		System.out.println(" = " + list1);
		
		list1.add(1, "b");
		System.out.println("tail = " + list1.tail);
		list1.add("c");
		SinglyLinkedHT<String> list3 = new SinglyLinkedHT<String>();
		System.out.println(list1 + " + " + list3);
		list1.append(list3);
		System.out.println(" = " + list1);
		
		SinglyLinkedHT<String> list4 = new SinglyLinkedHT<String>();
		list4.add(0, "z");
		list4.add(0, "y");
		list4.add(0, "x");		
		System.out.println(list1 + " + " + list4);
		list1.append(list4);
		System.out.println(" = " + list1);
		
		list1.remove(5);
		list1.remove(4);
		SinglyLinkedHT<String> list5 = new SinglyLinkedHT<String>();
		list5.add(0, "z");
		list5.add(0, "y");
		System.out.println(list1 + " + " + list5);
		list1.append(list5);
		System.out.println(" = " + list1);
	}
}