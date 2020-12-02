# Community website Development
This is a practice project, currently only in Chinese, and the English version will be added later.

The project is developed on the basis of **Spring boot**, MySQL, MyBatis...

- [28.11.2020 index](## 28.11.2020_index_site)
- [02.12.2020 register](## 02.12.2020_register)
## 28.11.2020_index_site 
* On this page, posts made by users will be displayed. In addition, the user's avatar, username, publishing time, etc. will be displayed. If the post is marked as essential or top, it will be displayed.
* And every page has only 10 posts will be displayed.
![](https://i.imgur.com/kHREX4p.png)

* Page number display
![](https://i.imgur.com/fGZEcGV.png)
![](https://i.imgur.com/hzTYZom.png)
and Only up to four pages will be displayed.

## 02.12.2020_register
* register
It is not allowed to use the same username and the same email address for registration. If used, a corresponding warning message will be given. 
![](https://i.imgur.com/k9zjbrG.png)

If the registration is successful, the user information will be uploaded to the database, and the user's mailbox will receive an email with an activation link.
![](https://i.imgur.com/QvJrg8g.png)

If you are the first time to click to activate, a successful activation page will be displayed, and the login page will be displayed after 8 seconds. If you are to repeatedly click to activate or use an invalid activation link, a corresponding warning page will be displayed.
