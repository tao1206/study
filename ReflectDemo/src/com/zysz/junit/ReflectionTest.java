package com.zysz.junit;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zysz.reflect.Person;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class ReflectionTest {
    @Test
    public void testClass() throws ClassNotFoundException {
        Class clazz = null;

        //1.通过类名获取Class对象
        clazz = Person.class;

        //2.通过对象名
        /*传入一个对象且不知对象类型时使用*/
        Object obj = new Person();
        clazz = obj.getClass();

        //3.通过全类名获取Class对象(会抛出异常)
        String className ="com.zysz.reflect.Person";
        clazz = Class.forName(className);

    }

    public Object getInstance() throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        //1.获取Class对象
        String className = "com.zysz.reflect.Person";
        //2.利用Class对象的newInstance方法创建一个类的实例
        Object obj = Class.forName(className).newInstance();

        return obj;
    }

    //类加载器
    @Test
    public void testClassLoader() throws ClassNotFoundException {
        //1.获取一个系统的类加载器(可以获取，当前类ReflectionTest就是系统的类加载器加载)
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        System.out.println(classLoader);

        //2.获取系统类加载器的父类加载器(扩展类加载器，可以获取)
        classLoader = classLoader.getParent();
        System.out.println(classLoader);

        //3.获取扩展类的父类加载器(引导类加载器，不可获取)
        classLoader = classLoader.getParent();
        System.out.println(classLoader);

        //4.测试当前类由哪一个加载器进行加载(系统类加载器)
        //4.1 获取当前类的Class对象
        Class clazz = Class.forName("com.zysz.junit.ReflectionTest");
        //4.2 获取类加载器
        classLoader = clazz.getClassLoader();
        System.out.println(classLoader);

        //5.测试JDK提供的Object类由哪一个加载器加载(引导类)
        classLoader = Class.forName("java.lang.Object").getClassLoader();
        System.out.println(classLoader);
    }

    //反射方法
    @Test
    public void testMethod() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        //获取类对象
        Class clazz = Class.forName("com.zysz.reflect.Person");
        /*
        * 3.1 获取clazz对应的所有方法--方法数组(-)
        * 不能获取private方法
        * */
        Method[] methods = clazz.getMethods();
        for (Method method:methods){
            System.out.print("  "+method.getName());
        }
        System.out.println();

        /*
         * 3.2 获取所有方法，包括私有方法： 方法数组(二)
         * 所有声明的方法都能获取，且只获取当前类的方法
         * */

        methods = clazz.getDeclaredMethods();
        for (Method method :methods){
            System.out.print("  "+method.getName());
        }
        System.out.println();

        /*
        * 3.3 获取指定的方法
        * 需要写参数名称和参数列表，无参则不需要书写
        * 例：public void setName(String name){}
        * */
        Method method = clazz.getDeclaredMethod("setName", String.class);
        System.out.println(method);
        /*
        * 对于方法public void setAge(int age)
        * 参数列表写成Integer是获取不到的，
        * 如果方法用于反射，那么要么int类型写成Integer：public void setAge(Integer age){}
        * 要么获取参数的方法写成int.class
        * */
        //method = clazz.getDeclaredMethod("setAge", Integer.class);
        method =clazz.getDeclaredMethod("setAge", int.class);
        System.out.println(method);

        //2.执行方法

        //2.1 将对象实例化
        Object obj = clazz.newInstance();
        //2.2 invoke的第一个参数表示执行哪个对象的方法，剩下的参数是执行方法时需要传入的参数
        method =obj.getClass().getDeclaredMethod("setName", String.class);
        method.invoke(obj,"张三");
        System.out.println(obj.getClass().getDeclaredMethod("getName").invoke(obj));

        /*
         * 2.2 执行私有方法
         * */
        Object object= clazz.newInstance();
        Method md =object.getClass().getDeclaredMethod("sayHello");
        //执行私有方法前需要加上method.setAccessible(true)
        md.setAccessible(true);
        md.invoke(obj);
    }

    /*
    * 把类对象和类方法名作为参数，执行方法
    * */

//    @Test
//    public void testInvoke() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Object object = new Person();
//        invoke(object,"test","wang",1);
//    }
//
//    public Object invoke(Object object,String methodName,Object...args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        //1.获取method对象
//        //因为getMethod的参数为Class列表类型，所以需要把参数args转化为Class类型
//        Class[] parameterType = new Class[args.length];
//        for (int i=0;i<args.length;i++){
//            parameterType[i]=args[i].getClass();
//            System.out.println(parameterType[i]);
//        }
//
//        //如果使用getDeclaredMethod，就不能获取父类方法，如果使用getMethod，就不能获取私有方法
//        Method method = object.getClass().getDeclaredMethod(methodName,parameterType);
//
//        //执行Method方法，并返回方法的返回值
//        return method.invoke(object,args);
//    }


//    @Test
//    public void testInvoke(){
//        invoke("com.zysz.reflect.Person","test","tao",1);
//
//    }

    public Object invoke(String className,String methodName,Object...args) {
        Object object = null;
        try {
            object = Class.forName(className).newInstance();
            //调用上一个方法
            return (Object) invoke((String) object, methodName,args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //使用系统方法(前提是有一个无参的构造器)
    @Test
    public void testInvoke(){
        Object result = invoke("java.text.SimpleDateFormat","format",new Date());
    }

    @Test
    public void testGetSuperClass() throws ClassNotFoundException {
        //1.获取Class对象
        String className ="com.zysz.reflect.Student";
        Class clazz = Class.forName(className);
        //获取父类对象
        Class superClass = clazz.getSuperclass();

        System.out.println(superClass);

    }

    //定义一个方法，既能访问当前类的私有方法也能访问父类的私有方法
    public Object invoke2(Object object,String methodName,Object...args){
        //1.获取Method对象
        Class[] parameterType = new Class[args.length];
        for (int i=0;i<args.length;i++){
            parameterType[i]=args[i].getClass();
        }

        try {
            //获取方法
            Method method = getMethod(object.getClass(), methodName, parameterType);
            method.setAccessible(true);

            //执行method方法
            return method.invoke(object, args);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Method getMethod(Class clazz,String methodName,Class...parameterType){

        /*
        * 获取clazz的methodName方法，该方法可能是私有方法，还可能在父类中
        * */
        for (;clazz!=Object.class;clazz = clazz.getSuperclass()){
            try {
                Method method = clazz.getDeclaredMethod(methodName,parameterType);
                return method;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //描述字段
    @Test
    public void testField() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String className ="com.zysz.reflect.Person";
        Class clazz = Class.forName(className);
        /*
        * 1.获取字段
        * 1.1获取所有的字段--字段数组
        * */
            Field[] fields = clazz.getDeclaredFields();
            for (Field feild :fields){
                System.out.print(feild);
            }
            System.out.println();
        /*
         *1.2 获取指定字段
         * */
        Field field = clazz.getDeclaredField("name");
        System.out.println(field.getName());

        Person person = new Person("张三",12);
        //2.使用字段
        //2.1执行私有字段
        field = clazz.getDeclaredField("age");
        field.setAccessible(true);
        System.out.println(field.get(person));
    }

    //描述构造器
    @Test
    public void testConstructor() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String className ="com.zysz.reflect.Person";
        Class<Person> clazz = (Class<Person>) Class.forName(className);

        //1.获取Constructor对象
        Constructor<Person>[] constructors = (Constructor<Person>[]) clazz.getConstructors();
        for (Constructor<Person> constructor :constructors){
            System.out.println(constructor);
        }
        //1.2 获取某一个，需要参数列表
        Constructor<Person> constructor = clazz.getConstructor(String.class,int.class);
        System.out.println(constructor);

        //1.3 调用构造器的newInstance()方法创建对象
        Object object = constructor.newInstance("zhang",1);

    }


}
