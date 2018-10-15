# LecturePicker
An automatic tool designed to sign up lectures in Fuzhou University.

# Library
> * [HtmlUnit](https://mvnrepository.com/artifact/net.sourceforge.htmlunit/htmlunit/2.33)
> * [Javax.Mail](https://mvnrepository.com/artifact/javax.mail/javax.mail-api/1.6.2)
> * [Jansi](https://mvnrepository.com/artifact/org.fusesource.jansi/jansi/1.17.1)

# Configure
All of these config files are encoding with utf-8.

### config.txt

|Line|  Description                              |
|:--:| :---------------------------------------: |
| 1  | Username for http://jwch.fzu.edu.cn       |
| 2  | Password for http://jwch.fzu.edu.cn       |
| 3  | QQ EMail Address                          |
| 4  | Authorization Code for your qq email      |

### keyword.txt

You should write down your keywords line by line.
```
If BLACKLIST mode is enabled, lectures containing key words will be filtered.
If WHITELIST mode is enabled, program will only sign up for those lectures contain key words.
```

### record.txt

**DO NOT TOUCH IT.**
```
It will configured automaticly by the program.
Keep it on utf-8 encoding, or you will get unknown errors.
```



