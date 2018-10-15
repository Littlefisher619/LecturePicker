# LecturePicker
An automatic tool designed to sign up lectures in Fuzhou University.

# Feature
> * Highly customizable, you can specify the interval of scheduled task.
> * Toggle whitelist mode or blacklist mode depends on you.
> * Mail Notification Service
> * Colorful console outputs

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

DO NOT TOUCH IT.
```
It will configured automaticly by the program.
Keep it on utf-8 encoding, or you will get unknown errors.
```

# Command-line Arguments
|  Args      |  Description                               |
|:--------:  | :---------------------------------------:  |
| nomail     | Disable the mail notification service      |
| whitelist  | Enable the whitelist mode                  |
| debug      | Output debug messages                      |
| i=X        | set check interval to X seconod(s)         |

PS:

*If you dont specify the whitelist argument, blacklist mode will enabled by default.*

*The order of arguments can be ignored!*
```
Examples:

java -jar LecturePicker.jar i=600 nomail
java -jar LecturePicker.jar debug whitelist
```
# Attention
The disputes arising from the use of this software have nothing to do with the author.
*It is also including that you break the rules of your school and get punished!*
