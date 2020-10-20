package demo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

public class HelloClassLoader extends ClassLoader{
    public static void main(String[] args){
        try {
            new HelloClassLoader().class2xlass();
            Class<?> class0 = new HelloClassLoader().findClass("Hello");
            Object object = class0.newInstance();
            Method helloMethod = class0.getMethod("hello");
            helloMethod.invoke(object);
        } catch (IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        String path = this.getClass().getResource("/Hello.xlass").getPath();
        File file = new File(path);
        int length = (int) file.length();
        byte[] bytes =new byte[length];
        try{
            new FileInputStream(file).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("xlass文件");
        print(bytes);
        for(int i=0;i<length;i++){
            bytes[i]= (byte) (255-bytes[i]);
        }
        System.out.println("class文件");
        print(bytes);
        bytes = Base64.getEncoder().encode(bytes);
        print(bytes);
        bytes = Base64.getDecoder().decode(bytes);
        return defineClass(name,bytes,0,bytes.length);
    }
    public void class2xlass(){
        File classFile = new File(this.getClass().getResource("/Hello.txt").getPath());
        int length = (int) classFile.length();
        byte[] bytes = new byte[length];
        try {
            new FileInputStream(classFile).read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("class文件");
        print(bytes);
        for(int i=0;i<length;i++){
            bytes[i] = (byte) (255-bytes[i]);
        }
        System.out.println("xlass文件");
        print(bytes);
    }
    public byte[] decode(String input){
        return Base64.getDecoder().decode(input);
    }
    public void print(byte[] bytes){
        for(byte b:bytes)
            System.out.print(b);
        System.out.println();
    }
}
