# AopPermission

简书：

#####一、背景
AOP （面向切面编程），Aspect Oriented Programming的缩写，意为：面向切面编程，通过预编译方式和运行期间动态代理实现程序功能的统一维护的一种技术。AOP是OOP的延续，是软件开发中的一个热点，也是Spring框架中的一个重要内容，是函数式编程的一种衍生范型。利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。（百度百科）

Android权限，做Android开发的都知道，申请权限虽然简单，但是每次使用都写一次繁琐的申请流程。当然，github上面也有很多比较优秀的开源权限申请框架。

所谓“不想当将军的士兵不是好士兵”，“不想‘偷懒’的程序员不是好程序员”。因此，我本着偷懒偷到极致的想法，利用aspectjrt aop编程方式开发一个权限申请工具，做到在实际使用当中一行代码完成申请权限。（牛吹得有点过了）

项目github地址：https://github.com/wushaohongly/AopPermission

运行效果：
![申请权限](https://upload-images.jianshu.io/upload_images/16821601-05bf4d47f7b1e107.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![权限拒绝后](https://upload-images.jianshu.io/upload_images/16821601-e447f35d23636356.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



#####二、依赖配置
项目 build.gradle
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
app build.gradle
```
dependencies {
    ...
    implementation 'org.aspectj:aspectjrt:1.9.5'
    implementation 'com.github.wushaohongly:AopPermission:1.0.0'
}

// 最后增加
/**
 * aspectjrt 编译相关
 */
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

final def log = project.logger
final def variants = project.android.applicationVariants

// 在构建工程时，执行编辑
variants.all { variant ->

//    if (!variant.buildType.isDebuggable()) {
//        log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
//        return;
//    }

    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.8",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
        log.debug "ajc args: " + Arrays.toString(args)

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                    log.warn message.message, message.thrown
                    break;
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}
```
#####三、使用
以申请拨打电话与读外置存储权限为例，click是一个需要拥有权限才能执行的方法，在click方法加入@PermissionsCheck（权限名称字符串数组）注解，执行click方法前就会申请注解生命的权限。

*如果申请成功，则会执行click方法体，否则将不执行click方法体。*
```
    @PermissionsCheck({Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE})
    public void click(View view) {
        Toast.makeText(this, "权限申请成功", Toast.LENGTH_LONG).show();
    }
```

#####四、支持配置拒绝权限弹框
在三，当用户拒绝权限后，因为不执行click方法体，没有其他友好性提示，显然是不合理的。为了增加扩展性，类库支持拒绝权限后弹出自定义Dialog。
自定义的Dialog须继承与类库的PermissionRefuseDialog。
```
public class MyRefuseDialog extends PermissionRefuseDialog {

    ...

    public MyRefuseDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.布局);
    }

    @Override
    public void setPermissions(String[] permissions) {
        // 父类赋值 permissions
        super.setPermissions(permissions);

        // 这里拿到拒绝的权限数组
    }
}
```
再BaseActivity / BaseFragment当中设置，此处为什么呢？因为Dialog是需要依赖Window，所以需要在具体的某个Activity / Fragment当中创建。（或者还有更好的方式实现）
```
        // 将自定义的Dialog，设置到 library中
        AopPermissionsConfig.setRefuseDialog(new MyRefuseDialog(this));
```

#####五、混淆配置
```
-keep class com.wushaohong.library.permissions.**{*;}
```

#####六、结语
1、实测是运行在Android10系统，据了解，aspectjrt1.9.5最低支持Android7.0
2、不支持Kotlin语言（kotlin版已实现，有时间再整理上传）
3、如有不合理之处，欢迎各位指点，本人感激不尽。
