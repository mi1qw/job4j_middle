package ru.job4j.ajax.servlet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class GreetingServlet extends HttpServlet {
    public static final Logger LOGGER = LoggerFactory.getLogger(GreetingServlet.class);
    public static final MyJson JSON = new MyJson();

    /**
     * doGet.
     *
     * @param req  req
     * @param resp resp
     */
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) {
        String name = req.getParameter("name");
        out("Nice to meet you, " + name, resp);
    }

    private void out(final String str, final HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try (PrintWriter writer = new PrintWriter(resp.getOutputStream())) {
            writer.println(str);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * doPost.
     *
     * @param req  req
     * @param resp resp
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) {
        String res = null;
        try (InputStream in = req.getInputStream()) {
            res = new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        User user = JSON.fromJson(res);
        LOGGER.info(user.toString());
        out(JSON.toJson(user), resp);
    }

    static class User {
        private String name;
        private String surname;
        private String sex;
        private String comment;
        private String email;
        private String pwd;

        User() {
        }

        User(final String name, final String surname, final String sex, final String comment,
             final String email, final String pwd) {
            this.name = name;
            this.surname = surname;
            this.sex = sex;
            this.comment = comment;
            this.email = email;
            this.pwd = pwd;
        }

        User(final String name, final String surname, final String email) {
            this.name = name;
            this.surname = surname;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(final String surname) {
            this.surname = surname;
        }

        @JsonIgnore
        public String getSex() {
            return sex;
        }

        @JsonProperty
        public void setSex(final String sex) {
            this.sex = sex;
        }

        @JsonIgnore
        public String getComment() {
            return comment;
        }

        @JsonProperty
        public void setComment(final String comment) {
            this.comment = comment;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(final String email) {
            this.email = email;
        }

        @JsonIgnore
        public String getPwd() {
            return pwd;
        }

        @JsonProperty
        public void setPwd(final String pwd) {
            this.pwd = pwd;
        }

        @Override
        public String toString() {
            return "User{"
                    + "name='" + name + '\''
                    + ", surname='" + surname + '\''
                    + ", sex='" + sex + '\''
                    + ", comment='" + comment + '\''
                    + ", email='" + email + '\''
                    + ", pwd='" + pwd + '\'' + '}';
        }
    }
}


