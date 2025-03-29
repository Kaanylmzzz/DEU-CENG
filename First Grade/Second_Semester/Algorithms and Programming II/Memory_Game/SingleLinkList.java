import java.io.FileWriter;
import java.io.IOException;

public class SingleLinkList {
    public Node head;

    public void add(Object dataToAdd) {
        Node newNode = new Node(dataToAdd);
        if (head == null) {
            head = newNode;
        }
        else {
            Node temp = head;
            while (temp.getLink() != null) {
                temp = temp.getLink();
            }
            temp.setLink(newNode);
        }
    }

    //The part where I update the high score table according to the added score.
    public void update_highscore (Object dataToAdd, Object dataToAdd2, SingleLinkList SLL3, SingleLinkList SLL4) {
        Node head = SLL4.getHead();
        Node head2 = SLL3.getHead();
        if (head == null || ((Integer) head.getData() < (Integer)dataToAdd)) {
            Node newNode = new Node(dataToAdd);
            Node newNode2 = new Node(dataToAdd2);
            newNode.setLink(SLL4.getHead());
            newNode2.setLink(SLL3.getHead());
            SLL4.setHead(newNode);
            SLL3.setHead(newNode2);

        } else {
            Node previous = null;
            Node previous2 = null;
            while (head != null && (Integer) head.getData() >= (Integer)dataToAdd) {
                previous = head;
                previous2 = head2;
                head = head.getLink();
                head2 = head2.getLink();
            }
            Node newNode = new Node(dataToAdd);
            Node newNode2 = new Node(dataToAdd2);
            newNode.setLink(previous.getLink());
            newNode2.setLink(previous2.getLink());
            previous.setLink(newNode);
            previous2.setLink(newNode2);
        }
    }
    //The part I was sorting.
    public void sorted_add(SingleLinkList TempSLL4, SingleLinkList SLL4, SingleLinkList TempSLL3, SingleLinkList SLL3) {
        while (TempSLL4.getHead() != null) {
            Node head = SLL4.getHead();
            Node head2 = SLL3.getHead();
            Node temp = TempSLL4.getHead();
            Node temp2 = TempSLL3.getHead();
            if (head == null || (Integer) head.getData() <= (Integer) temp.getData()) {
                Node newNode = new Node(temp.getData());
                Node newNode2 = new Node(temp2.getData());
                newNode.setLink(SLL4.getHead());
                newNode2.setLink(SLL3.getHead());
                SLL4.setHead(newNode);
                SLL3.setHead(newNode2);
                TempSLL4.setHead(temp.getLink());
                TempSLL3.setHead(temp2.getLink());
            } else {
                Node previous = null;
                Node previous2 = null;
                while (head != null && (Integer) head.getData() > (Integer) temp.getData()) {
                    previous = head;
                    previous2 = head2;
                    head = head.getLink();
                    head2 = head2.getLink();
                }
                Node newNode = new Node(temp.getData());
                Node newNode2 = new Node(temp2.getData());
                newNode.setLink(previous.getLink());
                newNode2.setLink(previous2.getLink());
                previous.setLink(newNode);
                previous2.setLink(newNode2);
                TempSLL4.setHead(temp.getLink());
                TempSLL3.setHead(temp2.getLink());
            }
        }
    }

    public int size (){
        if (head == null)
            return 0;
        else {
            Node temp = head;
            int count = 0;
            while (temp != null){
                count++;
                temp = temp.getLink();
            }
            return count;
        }
    }
    public void delete(Object dataToDelete){
        if (head == null)
            System.out.println("List is empty.");
        else {
            while (head.getData().equals(dataToDelete)){
                head = head.getLink();
            }
            Node temp = head;
            Node previous = null;
            while (temp != null){
                if (temp.getData().equals(dataToDelete)){
                    previous.setLink(temp.getLink());
                    temp = previous;
                }
                previous = temp;
                temp = temp.getLink();
            }
        }
    }

    public void display (){
        if (head == null)
            System.out.println("List is empty.");
        else {
            Node temp = head;
            while (temp != null){
                System.out.print(temp.getData() + " ");
                temp = temp.getLink();
            }
        }
    }
    //The part where I print the high score table on the console as well as while printing the text.
    public void displayScores (SingleLinkList SLL3, SingleLinkList SLL4){
        try {
            Node head = SLL4.getHead();
            Node head2 = SLL3.getHead();
            int counter = 0;
            if (head == null)
                System.out.println("List is empty.");
            else {
                Node temp = head;
                Node temp2 = head2;
                FileWriter myWriter = new FileWriter("D:\\highscoretable.txt",false);
                while (temp != null && counter < 12){
                    myWriter.write(String.format("%-10s %-6s\n",temp2.getData(), temp.getData()));
                    System.out.print(String.format("%-10s %-6s\n",temp2.getData(), temp.getData()));
                    temp = temp.getLink();
                    temp2 = temp2.getLink();
                    counter++;
                }
                myWriter.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

}
