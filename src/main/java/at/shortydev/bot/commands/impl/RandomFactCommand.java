package at.shortydev.bot.commands.impl;

import at.shortydev.bot.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

public class RandomFactCommand extends Command {

    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private final List<String> facts = new ArrayList<>();

    public RandomFactCommand(String name, String description, String... aliases) {
        super(name, description, aliases);

        facts.addAll(Arrays.asList("Shorty isn't gay.",
                "Julian is cool.",
                "Thorlak isn't cool.",
                "Chivitos makes papitas.",
                "yooniks best coder.",
                "Banging your head against a wall for one hour burns 150 calories.",
                "In Switzerland it is illegal to own just one guinea pig.",
                "Pteronophobia is the fear of being tickled by feathers.",
                "Snakes can help predict earthquakes.",
                "Crows can hold grudges against specific individual people.",
                "The oldest \"your mom\" joke was discovered on a 3,500 year old Babylonian tablet.",
                "So far, two diseases have successfully been eradicated: smallpox and rinderpest.",
                "29th May is officially \"Put a Pillow on Your Fridge Day\".",
                "Cherophobia is an irrational fear of fun or happiness.",
                "7% of American adults believe that chocolate milk comes from brown cows.",
                "If you lift a kangaroo’s tail off the ground it can’t hop.",
                "Bananas are curved because they grow towards the sun.",
                "Most Korean people don’t have armpit odor.",
                "The original London Bridge is now in Arizona.",
                "During your lifetime, you will produce enough saliva to fill two swimming pools.",
                "If Pinocchio says \"My Nose Will Grow Now\", it would cause a paradox.",
                "Polar bears could eat as many as 86 penguins in a single sitting…",
                "Car manufacturer Volkswagen makes sausages.",
                "Movie trailers were originally shown after the movie, which is why they were called \"trailers\".",
                "An eagle can hunt down a young deer and fly away with it.",
                "The smallest bone in your body is in your ear.",
                "Tennis players are not allowed to swear when they are playing in Wimbledon.",
                "Only 5% of the ocean has been explored.",
                "The top six foods that make your fart are beans, corn, bell peppers, cauliflower, cabbage and milk.",
                "Canadians say \"sorry\" so much that a law was passed in 2009 declaring that an apology can’t be used as evidence of admission to guilt.",
                "Back when dinosaurs existed, there used to be volcanoes that were erupting on the moon.",
                "The only letter that doesn’t appear on the periodic table is J",
                "In 2006, a Coca-Cola employee offered to sell Coca-Cola secrets to Pepsi. Pepsi responded by notifying Coca-Cola.",
                "In 2006, a Coca-Cola employee offered to sell Coca-Cola secrets to Pepsi. Pepsi responded by notifying Coca-Cola.",
                "If you point your car keys to your head, it increases the remote’s signal range.",
                "Princess Peach didn’t move until 1988, designers believed it was too complicated to make her a movable character.",
                "The world’s largest grand piano was built by a 15-year-old in New Zealand.",
                "There is a boss in Metal Gear Solid 3 that can be defeated by not playing the game for a week; or by changing the date.",
                "German Chocolate Cake is named after an American baker by the name of Samuel German.",
                "Apple paid a couple $1.7 million dollars for their plot of land, which was only worth $181,700.",
                "There is an underwater version of rugby, unsurprisingly called \"underwater rugby\".",
                "65% of autistic kids are left-handed, and only 10% of people in general are left-handed.",
                "The average American child is given $3.70 per tooth that falls out.",
                "\"Opposites attract\" is a common myth. People are actually attracted to people who look like family members, or those with a similar personality type.",
                "The most expensive virtual object is \"Club NEVERDIE\" in the Entropia Universe which is worth $635,000. It was originally bought at $10,000.",
                "Norway has a 25 year statute of limitation on murder. This means if the murder happened more than 25 years ago, they cannot be charged.",
                "\"Tsundoku\" is a Japanese word for the habit of buying too many books, letting them pile up in your house, and never reading them.",
                "Brain fibers lose 10% of their total length every decade. They can shrink even more so under acute stress.",
                "In Japan, Domino’s started testing pizza delivery via reindeer in 2016.",
                "Samsung means \"three stars\" in Korean. This was chosen by the founder because he wanted the company to be powerful and everlasting like stars in the sky.",
                "On average, 46.1% of Americans have less than $10,000 in assets when they die.",
                "Videogames have been found to be more effective at battling depression than therapy.",
                "In 2005, Connecticut was accidentally issued an Emergency Alert to evacuate the entire state. Only about 1% of the people actually tried to leave.",
                "Somebody hid an episode of South Park inside Tiger Woods 99 as an Easter egg, causing EA to do a massive recall.",
                "A study from Harvard University finds that having no friends can be just as deadly as smoking. Both effect levels of a blood-clotting protein.",
                "In Korea, there is a breed of dog called a Sapsali which was originally thought to banish ghosts and evil spirits."
        ));
    }

    @Override
    public void onCommand(TextChannel channel, Member member, String command, String[] args, Message message) {
        channel.sendMessage(facts.stream().skip(RANDOM.nextInt(facts.size())).findAny().orElse("This is weird...")).queue();
    }
}
