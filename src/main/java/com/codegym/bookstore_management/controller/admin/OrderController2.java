package com.codegym.bookstore_management.controller.admin;

import com.codegym.bookstore_management.service.OrderService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import com.codegym.bookstore_management.model.Order;

@Controller
@RequestMapping("/admin/orders")
public class OrderController2 {
    private final OrderService orderService;

    public OrderController2(OrderService orderService) {
        this.orderService = orderService;
    };

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderService.findAllOrders());
        return "admin/order/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.findOrderById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "admin/order/form";
        } else {
            return "redirect:/admin/orders";
        }
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute("order") Order order) {
        Optional<Order> existingOrderOpt = orderService.findOrderById(order.getId());
        if (existingOrderOpt.isPresent()) {
            Order existingOrder = existingOrderOpt.get();
            existingOrder.setStatus(order.getStatus());
            orderService.saveOrder(existingOrder);
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/detail/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        Optional<Order> orderOpt = orderService.findOrderById(id);
        if (orderOpt.isPresent()) {
            model.addAttribute("order", orderOpt.get());
            return "admin/order/detail";
        } else {
            return "redirect:/admin/orders";
        }
    }
}