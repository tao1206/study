package com.zysz.reflect;


public class Person {


     String name;
    private int age;


    //带参构造器


    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    //不带参构造器
    public Person() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    /**
     * set和get方法
     */



    private void sayHello(){
        System.out.println("HelloWord");
    }

    public void test(String name,Integer age){
        System.out.println("调用成功");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
