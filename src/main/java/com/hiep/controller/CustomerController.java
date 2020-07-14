package com.hiep.controller;

import com.hiep.model.Customer;
import com.hiep.model.Province;
import com.hiep.service.customer.CustomerService;
import com.hiep.service.province.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.findAll();
    }

    @GetMapping("/create-customer")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        return modelAndView;
    }

    @PostMapping("/create-customer")
    public ModelAndView saveCustomer(@Validated@ModelAttribute("customer") Customer customer, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()) {
            ModelAndView modelAndView = new ModelAndView("/customer/create");
            return modelAndView;
        }
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("messages", "New customer created successfully");
        return modelAndView;
    }

//    Optional để nói rằng tham số s có huwajc ko
    @GetMapping("/customers")
    public ModelAndView listCustomers(@PageableDefault(size = 3) Pageable pageable, @RequestParam("search") Optional<String> search){
//    public ModelAndView listCustomers(@PageableDefault(size = 3, direction = Sort.Direction.DESC, sort = "id") , Pageable pageable){
        Page<Customer> customers;
        ModelAndView modelAndView = new ModelAndView("/customer/list");
        if(search.isPresent()){
            customers = customerService.findAllByFirstNameContaining(search.get(), pageable);
            modelAndView.addObject("s", search.get());
        } else {
            customers = customerService.findAll(pageable);
        }
//        customers = customerService.findAll(pageable);
        modelAndView.addObject("customers", customers);

        return modelAndView;
    }

    @GetMapping("/edit-customer/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if(customer != null) {
            ModelAndView modelAndView = new ModelAndView("/customer/edit");
            modelAndView.addObject("customer", customer);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-customer")
    public ModelAndView updateCustomer(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView = new ModelAndView("/customer/edit");
        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-customer/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id){
        Customer customer = customerService.findById(id);
        if(customer != null) {
            ModelAndView modelAndView = new ModelAndView("/customer/delete");
            modelAndView.addObject("customer", customer);
            return modelAndView;

        }else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-customer")
    public String deleteCustomer(@ModelAttribute("customer") Customer customer) {
        customerService.remove(customer.getId());
        return "redirect:customers";
    }

//    @PostMapping("/validateCustomer")
//    public ModelAndView checkValidation(@Validated@ModelAttribute("customer") Customer customer, BindingResult bindingResult){
//        if (bindingResult.hasFieldErrors()) {
//            ModelAndView modelAndView = new ModelAndView("/customer/create");
//            return modelAndView;
//        }
//        ModelAndView modelAndView = new ModelAndView("/customer/create");
//        modelAndView.addObject("message",new String("Tạo thành công"));
//        return modelAndView;
//    }

// Bo dau cach hai dau
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        StringTrimmerEditor stringTrimmerEditor=new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class,stringTrimmerEditor);
    }

}
