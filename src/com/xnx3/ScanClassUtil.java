package com.xnx3;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 扫描某个包内的 Class 类、注解
 * @author 管雷鸣
 *
 */
public class ScanClassUtil {
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException {
     // 包下面的类
        List<Class<?>> classList = getClasses("com.xnx3.wangmarket");
        for (int i = 0; i < classList.size(); i++) {
			System.out.println(classList.get(i).getName());
		}
        
        List<Class<?>> c = getClassSearchAnnotationsName(classList, "PluginRegister");
        System.out.println(c.get(0).getName());

	}
	
	/**
	 * 从 指定的一堆 {@link Class} 中，找出某个接口的实现类，放入 List 返回。只要类中有过实现这个接口，便将这个类返回
	 * @param classList 要找的一堆 {@link Class}
	 * @param interfaceName 如：com.xnx3.wangmarket.weixin.interfaces.AutoReply
	 * @return 将有此接口实现的类用 List 返回
	 */
	public static List<Class<?>> searchByInterfaceName(List<Class<?>> classList, String interfaceName){
        Iterator<Class<?>> iterator = classList.iterator();
        List<Class<?>> list = new ArrayList<Class<?>>();
        while (iterator.hasNext()) {
            Class<?> cls = iterator.next();
            Class<?>[] ic = cls.getInterfaces();
            for (int i = 0; i < ic.length; i++) {
				Class<?> c = ic[i];
				if(c.getName().equals(interfaceName)){
					list.add(cls);
				}
			}
        }
        return list;
	}
	
	/**
	 * 从 指定的一堆 {@link Class} 中，找出类上有某个指定注解的类，放入 List 返回。
	 * <pre>
	 * //使用如:
	 * List&lt;Class &lt?&gt;&gt; c = getClassSearchAnnotationsName(classList, "PluginRegister");
	 * </pre>
	 * @param classList 要找的一堆 {@link Class}
	 * @param annotationClassSimpleName 注解的类的名字，如注解类为：<p> public @interface PluginRegister {} </p> ，则此处传入: PluginRegister ，不含包及路径，纯脆名字
	 * @return 将有此注解的类用 List 返回
	 */
	public static List<Class<?>> getClassSearchAnnotationsName(List<Class<?>> classList, String annotationClassSimpleName){
        List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> clazz : classList) {
            // 获取类上的注解
            Annotation[] annos = clazz.getAnnotations();
            
            for (int j = 0; j < annos.length; j++) {
				if(annos[j].annotationType().getSimpleName().equals(annotationClassSimpleName)){
					//找到指定的类了
					list.add(clazz);
				}
			}
        }
        
		return list;
	}
	
	/**
	 * <pre>
	 * // 某类或者接口的子类
     *  List&lt;Class &lt?&gt;&gt; inInterface = getByInterface(Object.class, classList);
     *  System.out.println("int-->"+inInterface.size() + "");
	 * </pre>
	 * @param clazz
	 * @param classesAll
	 * @return
	 * @deprecated
	 */
    public static List<Class<?>> getByInterface(Class clazz, List<Class<?>> classesAll) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        // 获取指定接口的实现类
        if (!clazz.isInterface()) {
            try {
                /**
                 * 循环判断路径下的所有类是否继承了指定类 并且排除父类自己
                 */
                Iterator<Class<?>> iterator = classesAll.iterator();
                while (iterator.hasNext()) {
                    Class<?> cls = iterator.next();
                    if (clazz.isAssignableFrom(cls)) {
                        if (!clazz.equals(cls)) {
                        	classList.add(cls);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classList;
    }


    /**
     * 从包package中获取所有的Class。如：
     *	<pre>
     *		List&lt;Class &lt?&gt;&gt; classList = getClasses("com.xnx3");
     *	</pre>
     * @param pack 要搜索的包，如 com.xnx3
     * @return 类的List集合
     */
    public static List<Class<?>> getClasses(String pack) {
    	List<Class<?>> classList = new ArrayList<Class<?>>();
        // 是否循环迭代
        boolean recursive = true;
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            // 循环迭代下去
            while (dirs.hasMoreElements()) {
                // 获取下一个元素
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    // 以文件的方式扫描整个包下的文件 并添加到集合中
                    findAndAddClassesInPackageByFile(packageName, filePath, recursive, classList);
                } else if ("jar".equals(protocol)) {
                    // 如果是jar包文件
                    // 定义一个JarFile
                    // System.err.println("jar类型的扫描");
                    JarFile jar;
                    try {
                        // 获取jar
                        jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        // 从此jar包 得到一个枚举类
                        Enumeration<JarEntry> entries = jar.entries();
                        // 同样的进行循环迭代
                        while (entries.hasMoreElements()) {
                            // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // 如果是以/开头的
                            if (name.charAt(0) == '/') {
                                // 获取后面的字符串
                                name = name.substring(1);
                            }
                            // 如果前半部分和定义的包名相同
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // 如果以"/"结尾 是一个包
                                if (idx != -1) {
                                    // 获取包名 把"/"替换成"."
                                    packageName = name.substring(0, idx).replace('/', '.');
                                }
                                // 如果可以迭代下去 并且是一个包
                                if ((idx != -1) || recursive) {
                                    // 如果是一个.class文件 而且不是目录
                                    if (name.endsWith(".class") && !entry.isDirectory()) {
                                        // 去掉后面的".class" 获取真正的类名
                                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                                        try {
                                            // 添加到classes
                                        	classList.add(Class.forName(packageName + '.' + className));
                                        } catch (ClassNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("在扫描用户定义视图时从jar包获取文件出错");
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classList;
    }
    

    /**
     * 以文件的形式来获取包下的所有Class
     *
     * @param packageName
     * @param packagePath
     * @param recursive
     * @param classes
     */
    public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
            List<Class<?>> classes) {
        // 获取此包的目录 建立一个File
        File dir = new File(packagePath);
        // 如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn("用户定义包名 " + packageName + " 下没有任何文件");
            return;
        }
        // 如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
            }
        });
        // 循环所有文件
        for (File file : dirfiles) {
            // 如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
                        classes);
            } else {
                // 如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 添加到集合中去
                    // classes.add(Class.forName(packageName + '.' + className));
                    // 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
                    classes.add(
                            Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
                } catch (ClassNotFoundException e) {
                    // log.error("添加用户自定义视图类错误 找不到此类的.class文件");
                    e.printStackTrace();
                }
            }
        }
    }

	
}
