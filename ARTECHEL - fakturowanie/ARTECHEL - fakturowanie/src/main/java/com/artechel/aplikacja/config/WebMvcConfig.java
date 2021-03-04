package com.artechel.aplikacja.config;

import com.artechel.aplikacja.DAO.*;
import com.artechel.aplikacja.methods.CreatePDF;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.ResourceBundleViewResolver;

import java.util.Properties;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.artechel.aplikacja.controllers","com.artechel.aplikacja"})
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public DriverManagerDataSource getDataSource() {

        DriverManagerDataSource bds = new DriverManagerDataSource();
        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://database-1.clmaq626sr4p.eu-west-1.rds.amazonaws.com:3306/artechel.dbtest?useUnicode=yes&characterEncoding=UTF-8");
        bds.setUsername("root");
        bds.setPassword("PracaDyplomowa2)2)");

        return bds;
    }

    @Bean
    public ResourceBundleViewResolver resolve() {
        ResourceBundleViewResolver resolve = new ResourceBundleViewResolver();
        resolve.setBasename("pdf-views");
        return resolve;
    }

    @Bean
    public InternalResourceViewResolver resolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public InvoiceDAO getInvoiceDao() {
        return new InvoiceDAOImplementation(getDataSource());
    }

    @Bean
    public CustomerDAO getCustomerDao() {
        return new CustomerDAOImplementation(getDataSource());
    }

    @Bean
    public Invoice_productDAO getInvoice_productDAO() { return new Invoice_productDAOImplementation(getDataSource()); }

    @Bean
    public PayerDAO getPayerDAO() { return new PayerDAOImplementation(getDataSource()); }

    @Bean
    public ProductDAO getProductDAO() { return new ProductDAOImplementation(getDataSource());}

    @Bean
    public UsersDAO getUsersDAO() { return new UsersDAOImplementation(getDataSource());}

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587); //25
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setUsername("diplomaprojecttest@gmail.com");
        mailSender.setPassword("Test12345!@#$%");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");


        return mailSender;
    }

    @Bean
    public SimpleMailMessage emailTemplate()
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("ma.switala@gmail.com");
        message.setFrom("diplomaprojecttest@gmail.com");
        message.setSubject("Prośba o zresetowanie hasła");

        return message;
    }

}
