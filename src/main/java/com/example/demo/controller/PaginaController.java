/*package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {
    @GetMapping("/acerca")
    public String mostrarAcerca(){
        return "acerca";
    }
    @GetMapping("/joyeria")
    public String mostrarJoyerias(){
        return "joyeria";
    }

    @GetMapping("/juegos")
    public String mostrarJuegos(){
        return "juegos";
    }

    @GetMapping("/prendas")
    public String mostrarPrendas(){
        return "prendas";
    }

}
*/

package com.example.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.service.IProductoService;

@Controller
public class PaginaController {
    @Autowired
    IProductoService productoService;

    @GetMapping("/acerca")
    public String mostrarAcerca(){
        return "acerca";
    }
    @GetMapping("/joyeria")
    public String mostrarJoyerias(Model model){
        model.addAttribute("productos", productoService.cargarProductos());
        return "joyeria";
    }

    @GetMapping("/juegos")
    public String mostrarJuegos(Model model){
        model.addAttribute("productos", productoService.cargarProductos());
        return "juegos";
    }

    @GetMapping("/prendas")
    public String mostrarPrendas(Model model){
        model.addAttribute("productos", productoService.cargarProductos());
        return "prendas";
    }


}

