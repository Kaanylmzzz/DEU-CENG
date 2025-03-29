public class Stack {
    private int top;
    private Object elements[];

    public Stack(int Capacity){
        elements = new Object[Capacity];
        top = -1;
    }

    public void push(Object data){
        if (isFull())
            System.out.println("Stack overflow.");
        else{
            top++;
            elements[top] = data;
        }
    }

    public Object pop(){
        if (isEmpty()) {
            System.out.println("Stack is empty.");
            return null;
        }
        else{
            Object retData = elements[top];
            elements[top]= null;
            top--;
            return retData;
        }
    }

    public Object peek(){
        if (isEmpty()){
            System.out.println("Stack is empty.");
            return null;
        }
        else
             return elements[top];
    }

    public boolean isFull(){
        return top + 1 == elements.length;
    }

    public boolean isEmpty(){
        return top == -1;
    }

    public int size(){
        return top +1;
    }


}
