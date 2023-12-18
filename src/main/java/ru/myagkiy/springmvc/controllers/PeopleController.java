package ru.myagkiy.springmvc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.myagkiy.springmvc.models.Person;
import ru.myagkiy.springmvc.models.PersonDAO;

@Controller
@RequestMapping("/people")
@AllArgsConstructor
public class PeopleController {
    public PersonDAO personDAO;

    @GetMapping("index")
    public String index(Model model) {
        model.addAttribute("people", personDAO.index());
        return "people/index";
    }

    @GetMapping(value = "/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personDAO.show(id));
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") Person person) {
        personDAO.addPerson(person);
        return "redirect:/people/index";
    }

    @GetMapping("/edit/{id}")
    public String editPerson(@PathVariable("id") int id, Model model){
        model.addAttribute("person", personDAO.show(id));
        return "/people/edit";
    }

    @PatchMapping("/edit/{id}")
    public String editPerson(@ModelAttribute("person") Person person,
                             @PathVariable("id") int id) {
        personDAO.editPerson(person, id);
        return "redirect:/people/index";
    }
    @DeleteMapping("{id}")
    public String detelePerson(@PathVariable("id") int id){
        personDAO.deletePerson(id);
        return "redirect:/people/index";
    }
}
