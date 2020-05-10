## JVM Stash
Logstash Client for Java Virtual Machine

>##### Note (If you use TLS)
> You should convert your server certificate into PKCS12 format.

```shell
$ openssl pkcs12 -export -inkey your_server.key -in your_server.crt -out server.p12
```

### Usage

#### Basic
```java
Stash stash = new Stash.Builder()
                .setHost("localhost")
                .setPort(5000)
                .build();

try {
    stash.connect();
} catch (StashException e) {
    System.out.println(e.getMessage());
    System.exit(1);
}

stash.write("hello logstash".getBytes())

try {
    stash.close();
} catch (IOException e) {
    System.out.println(e.getMessage());
    System.exit(1);
}
```

#### TLS Connection

Assumed you already enable `ssl` config inside `logstash.conf`
```config
input {
	tcp {
		port => 5000
		ssl_enable => true
		ssl_cert => "/etc/server.crt"
		ssl_key => "/etc/server.key"
		ssl_verify => false
	}
}
```

Let's write some code again
```java
InputStream keyStore = null;

try {
    keyStore = new FileInputStream("/path/to/your/server.p12");
} catch (Exception e) {
    System.out.println(e.getMessage());
    System.exit(1);
}

Stash stash = new Stash.Builder()
                .setHost("localhost")
                .setPort(5000)
                // makesure set to true
                .setSecure(true)
                .setKeyStoreIs(keyStore)
                .setKeyStorePassword("damn12345")
                .build();

try {
    stash.connect();
} catch (StashException e) {
    System.out.println(e.getMessage());
    System.exit(1);
}

stash.write("hello logstash".getBytes())

try {
    stash.close();
} catch (IOException e) {
    System.out.println(e.getMessage());
    System.exit(1);
}
```

#### With Java's standar logging
```java
private static final Logger LOGGER = Logger.getLogger(AppLogger.class.getName());

public static void main(String[] args) throws StashException {
        InputStream keyStore = null;
        
        try {
            keyStore = new FileInputStream("/path/to/your/server.p12");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        Stash stash = new Stash.Builder()
                        .setHost("localhost")
                        .setPort(5000)
                        // makesure set to true
                        .setSecure(true)
                        .setKeyStoreIs(keyStore)
                        .setKeyStorePassword("damn12345")
                        .build();
        
        try {
            stash.connect();
        } catch (StashException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        
        // Set Handler with StashLogHandler
        LOGGER.addHandler(new StashLogHandler(stash));
        
        // Use standar log that uses StashLogHandler
        LOGGER.log(Level.INFO, "hello");
        
        try {
            stash.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
```

#### Spring Boot Integration
https://github.com/wuriyanto48/spring-boot-starter-jvmstash