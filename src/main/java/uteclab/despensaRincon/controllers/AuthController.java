package uteclab.despensaRincon.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uteclab.despensaRincon.models.services.EmailService;
import uteclab.despensaRincon.utils.JWTUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dr/auth")
public class AuthController {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Value("${admin.username}")
    private String usuario;

    @Value("${admin.password}")
    private String password;

    @CrossOrigin(origins = {"http://localhost:3000", "https://java-ee-frontend.vercel.app/"})
    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody ObjectNode JSONObject){

        Map<String, Object> response = new HashMap<>();
        ArrayList<String> error = new ArrayList<>();

        String user = null;
        String pass = null;

        try{
            user = JSONObject.get("username").asText();
            pass = JSONObject.get("password").asText();
        }catch (Exception e){
            response.put("msg", "Error al iniciar sesion!");
            response.put("error", "Debes indicar el username y la password");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }


        if(user.equals(this.usuario) && pass.equals(this.password)){
            String token = jwtUtil.create("374",this.usuario);
            response.put("token", token);
            response.put("msg", "Sesión creada correctamente");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
        }
        else{
            response.put("msg", "No se pudo crear la sesion");
            error.add("Credenciales invalidas");
            response.put("error", error);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
    }




    

}
