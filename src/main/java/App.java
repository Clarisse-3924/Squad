import Herosquard.Hero;
import Herosquard.Squad;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        staticFileLocation("/public");
        Hero hero1 =new Hero("Odile",30,"Eating ","sleeping");
        Hero hero2 =new Hero("Fabienne",30,"Dancing ","reading ");
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("username", request.session().attribute("username"));

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        post("/welcome", (request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();

            String inputtedUsername = request.queryParams("username");
            request.session().attribute("username", inputtedUsername);
            model.put("username", inputtedUsername);

            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

        //hero form
        get("/heroes/delete",(request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Hero.clearAll();
            model.put("heroes",Hero.getHeroes());
            return new ModelAndView(model,"hero-view.hbs");

        },new HandlebarsTemplateEngine());
        get("/create/hero",(request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView(model, "hero-form.hbs");

        },new HandlebarsTemplateEngine());

        post("/heroes/new", (request, response) -> { //URL to make new post on POST route
            Map<String, Object> model = new HashMap<>();

            String name = request.queryParams("name");
            int age=Integer.parseInt(request.queryParams("age"));
            String powers=request.queryParams("powers");
            String weakness=request.queryParams("weakness");

            Hero newHero = new Hero(name,age,powers,weakness);
            model.put("heroes", newHero);
            return new ModelAndView(model, "hero-form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/hero", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            ArrayList<Hero> heroes = Hero.getHeroes();
            model.put("heroes", heroes);

            return new ModelAndView(model, "hero-view.hbs");
        }, new HandlebarsTemplateEngine());


        get("/heroes/:id", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHeroToFind = Integer.parseInt(request.params(":id")); //pull id - must match route segment
            Hero foundHero = Hero.findById(idOfHeroToFind); //use it to find post
            model.put("hero", foundHero); //add it to model for template to display
            ArrayList<Hero> heroes = Hero.getHeroes();
            model.put("heroes", heroes);
            return new ModelAndView(model, "hero-view.hbs"); //individual post page.
        }, new HandlebarsTemplateEngine());

        //squad
        get("/squads/delete",(request, response) -> {
            Map<String, Object> model = new HashMap<String, Object>();
            Squad.clearAll();
            ArrayList<Hero> heroes=Hero.getHeroes();
            for (int i=0;i<heroes.size();i++){
                heroes.get(i).updateHero(false);
            }
            model.put("squads",Squad.getSquads());
            return new ModelAndView(model,"squad-view.hbs");

        },new HandlebarsTemplateEngine());

        get("/create/squad",(request, response) -> {
            Map<String, Object> model = new HashMap<>();
            ArrayList<Hero> heroes=Hero.getHeroes();
            ArrayList<Hero> heroList=new ArrayList<>();
            for (int i=0;i<heroes.size();i++){
                if(heroes.get(i).isOccupied()==false){
                    heroList.add(heroes.get(i));
                }
            }

            model.put("heroes",Hero.getHeroes());
            return new ModelAndView(model,"squad-form.hbs");
        },new HandlebarsTemplateEngine());
        post("/squads/new", (request, response) -> { //URL to make new post on POST route
            Map<String, Object> model = new HashMap<>();

            String name = request.queryParams("name");
            int maxSize=Integer.parseInt(request.queryParams("size"));
            String cause=request.queryParams("cause");
            ArrayList<Hero> heroes=new ArrayList<>();
            if(request.queryParamsValues("heroes")!=null){
                String[] heroesList=request.queryParamsValues("heroes");

                for(int i=0;i<heroesList.length;i++){
                    Hero addHero=Hero.findById(Integer.parseInt(heroesList[i]));
                    if(heroes.size()<maxSize){
                        addHero.updateHero(true);
                        heroes.add(addHero);
                    }

                }
            }
            Squad newSquad= new Squad(maxSize,name,cause,heroes);

            model.put("heroes",Hero.getHeroes());

            return new ModelAndView(model, "squad-form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/squad",(request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("squads",Squad.getSquads());
            return new ModelAndView(model,"squad-view.hbs");

        },new HandlebarsTemplateEngine());

        get("/squads/:id",(request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSquadToFind=Integer.parseInt(request.params(":id"));
            Squad foundSquad=Squad.findById(idOfSquadToFind);
            model.put("squad",foundSquad);
            ArrayList<Squad> squads=Squad.getSquads();
            model.put("squads",squads);
            return new ModelAndView(model,"squad-view.hbs");
        },new HandlebarsTemplateEngine());


        //delete hero

        get("/hero/:id/delete",(request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfHeroToDelete=Integer.parseInt(request.params(":id"));
            Hero foundHero=Hero.findById(idOfHeroToDelete);
            for (int i=idOfHeroToDelete;i<Hero.getHeroes().size();i++){
                Hero.getHeroes().get(i).setId(Hero.getHeroes().get(i).getId()-1);
            }
            foundHero.deleteHero();
            ArrayList<Hero> heroes = Hero.getHeroes();
            model.put("heroes", heroes);
            return new ModelAndView(model,"hero-view.hbs");

        },new HandlebarsTemplateEngine());
        get("/squad/:id/delete",(request, response) -> {
            Map<String, Object> model = new HashMap<>();
            int idOfSquadToDelete=Integer.parseInt(request.params(":id"));
            Squad foundSquad=Squad.findById(idOfSquadToDelete);
            ArrayList<Hero> heroes=foundSquad.getHeroes();

            for(int i=0;i<heroes.size();i++){
                heroes.get(i).updateHero(false);
            }
            for (int i=idOfSquadToDelete;i<Squad.getSquads().size();i++){
                Squad.getSquads().get(i).setId(Squad.getSquads().get(i).getId()-1);
            }
            foundSquad.deleteSquad();

            ArrayList<Squad> squads = Squad.getSquads();
            model.put("squads", squads);
            return new ModelAndView(model,"squad-view.hbs");

        },new HandlebarsTemplateEngine());






    }
}
