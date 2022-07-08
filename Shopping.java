import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class Shopping {

    private static List<String> categories = new LinkedList<String>() {
        {
            add("Candy");
            add("Snack");
            add("Drink");
            add("Pet");
            add("Meat");
            add("Bread/Pasta");
            add("Egg/Dairy");
            add("Condiment");
            add("Dessert");
            add("Kitchen");
            add("Bathroom");
            add("Other");
        }
    };

    private static Map<String, List<Item>> items = new HashMap<String, List<Item>>() {
        {
            put("Candy", new LinkedList<Item>() {
                {
                    add(new Item("Dark Chocolate M&M's", "Vons", "Candy"));
                    add(new Item("Crispy M&M’s", "Vons", "Candy"));
                    add(new Item("Hot Tamales", "Vons", "Candy"));
                    add(new Item("Dark Choc Peanut Butter Cups", "Trader Joe's", "Candy"));
                    add(new Item("Sea Salt Caramels", "Costco", "Candy"));
                    add(new Item("Sixlets", "Home Goods", "Candy"));
                    add(new Item("Smarties", "Home Goods", "Candy"));
                    add(new Item("Fun Dip", "Smart & Final", "Candy"));
                    add(new Item("Orange Tic Tacs", "Smart & Final", "Candy"));
                    add(new Item("Oreo Pieces", "Smart & Final", "Candy"));
                    add(new Item("Vanilla Taffy", "Amazon", "Candy"));
                    add(new Item("Tootsie Rolls", "Smart & Final", "Candy"));
                }
            });
            put("Snack", new LinkedList<Item>() {
                {
                    add(new Item("Goldfish", "Vons", "Snack"));
                    add(new Item("Salted Sunflower Seeds", "Vons", "Snack"));
                    add(new Item("Salted Peanuts", "Vons", "Snack"));
                    add(new Item("Vinegar Chips", "Vons", "Snack"));
                    add(new Item("BBQ Chips", "Vons", "Snack"));
                    add(new Item("Saltine Crackers", "Vons", "Snack"));
                    add(new Item("Buttered Popcorn", "Vons", "Snack"));
                    add(new Item("Altar Bread Wafers", "Amazon", "Snack"));
                }
            });
            put("Drink", new LinkedList<Item>() {
                {
                    add(new Item("Soda (Pepsi or Coke)", "Vons", "Drink"));
                    add(new Item("Godiva Dark Chocolate Liquor", "Total Wine & More", "Drink"));
                    add(new Item("Vita Divine Creme de Cacao", "Total Wine & More", "Drink"));
                    add(new Item("Kavlana Creme de Cacao", "Bevmo", "Drink"));
                    add(new Item("Absolut Vanilla Vodka", "Total Wine & More", "Drink"));
                    add(new Item("Smirnoff Vanilla Vodka", "Vons", "Drink"));

                }
            });
            put("Pet", new LinkedList<Item>() {
                {
                    add(new Item("Cat Food", "Vons", "Pet"));
                    add(new Item("Cat Litter", "Costco", "Pet"));
                    add(new Item("Electro-nectar", "Amazon", "Pet"));

                }
            });
            put("Meat", new LinkedList<Item>() {
                {
                    add(new Item("Pepperoni Slices", "Vons", "Meat"));
                    add(new Item("Sliced Ham", "Vons", "Meat"));
                    add(new Item("Sliced Turkey", "Vons", "Meat"));
                    add(new Item("Steak", "Vons", "Meat"));
                    add(new Item("Hamburger", "Vons", "Meat"));
                    add(new Item("Hot Dog", "Vons", "Meat"));
                    add(new Item("Teriyaki Beef Jerky", "Costco", "Meat"));

                }
            });
            put("Bread/Pasta", new LinkedList<Item>() {
                {
                    add(new Item("Hard Rolls", "Vons", "Bread/Pasta"));
                    add(new Item("Thomas English Muffins", "Vons", "Bread/Pasta"));
                    add(new Item("Sourdough Sliced Bread", "Vons", "Bread/Pasta"));
                    add(new Item("Buns (Hamburger/Hot Dog)", "Vons", "Bread/Pasta"));
                    add(new Item("Tortillas", "Vons", "Bread/Pasta"));
                    add(new Item("Hard Taco Shells", "Vons", "Bread/Pasta"));
                    add(new Item("Pasta", "Vons", "Bread/Pasta"));
                    add(new Item("Gnocchi", "Vons", "Bread/Pasta"));
                    add(new Item("Annie's Mac & Cheese", "Vons", "Bread/Pasta"));
                }
            });
            put("Egg/Dairy", new LinkedList<Item>() {
                {
                    add(new Item("Eggs", "Vons", "Egg/Dairy"));
                    add(new Item("Butter", "Vons", "Egg/Dairy"));
                    add(new Item("Half & Half", "Vons", "Egg/Dairy"));
                    add(new Item("Honey Yogurt", "Vons", "Egg/Dairy"));
                    add(new Item("String Cheese", "Vons", "Egg/Dairy"));
                    add(new Item("Shredded Cheese", "Vons", "Egg/Dairy"));
                    add(new Item("Sliced Cheese", "Vons", "Egg/Dairy"));
                    add(new Item("Black Diamond Cheese", "Vons", "Egg/Dairy"));
                }
            });
            put("Condiment", new LinkedList<Item>() {
                {
                    add(new Item("Pasta Sauce (w/ Meat)", "Vons", "Condiment"));
                    add(new Item("A1 Sauce", "Vons", "Condiment"));
                    add(new Item("Soy Sauce", "Vons", "Condiment"));
                    add(new Item("Ketchup", "Vons", "Condiment"));
                    add(new Item("Mustard", "Vons", "Condiment"));
                    add(new Item("Peanut Butter", "Vons", "Condiment"));
                    add(new Item("Honey", "Vons", "Condiment"));
                    add(new Item("Caramel Sauce", "Vons", "Condiment"));
                    add(new Item("Chocolate Syrup", "Vons", "Condiment"));
                    add(new Item("Victoria’s Mild Taco Sauce", "Vons", "Condiment"));
                    add(new Item("Frank’s Red Hot Sauce", "Vons", "Condiment"));
                    add(new Item("Sour Cream", "Vons", "Condiment"));
                    add(new Item("Laura Scudder's Ranch Dip Mix", "Vons", "Condiment"));
                    add(new Item("Taco Seasoning", "Vons", "Condiment"));
                    add(new Item("Vegetable Oil", "Vons", "Condiment"));
                    add(new Item("Extra Virgin Olive Oil", "Vons", "Condiment"));
                    add(new Item("Himilayan Salt Shaker", "Trader Joe's", "Condiment"));
                    add(new Item("Rainbow Black Pepper", "Trader Joe's", "Condiment"));

                }
            });
            put("Dessert", new LinkedList<Item>() {
                {
                    add(new Item("Vanilla Extract", "Vons", "Dessert"));
                    add(new Item("Brown Sugar", "Vons", "Dessert"));
                    add(new Item("Semi-Sweet Choc Chips", "Vons", "Dessert"));
                    add(new Item("Sprinkles", "Vons", "Dessert"));
                    add(new Item("Choc Malted Crunch Ice Cream", "Rite Aid", "Dessert"));
                    add(new Item("Chocolate Muffins", "Vons", "Dessert"));
                    add(new Item("Krusteaz Gingerbread Cookie Mix", "Vons", "Dessert"));
                    add(new Item("Gluten-Free Choc Chip Cookie Mix", "Stater Bros", "Dessert"));
                    add(new Item("Malted Milk Mix", "Stater Bros", "Dessert"));
                    add(new Item("Oreo Pie Mix", "Amazon", "Dessert"));

                }
            });
            put("Kitchen", new LinkedList<Item>() {
                {
                    add(new Item("Aluminum Foil", "Vons", "Kitchen"));
                    add(new Item("Paper Plates (Small)", "Vons", "Kitchen"));
                    add(new Item("Paper Plates (Large)", "Vons", "Kitchen"));
                    add(new Item("Zip-Lock Bags (Small)", "Vons", "Kitchen"));
                    add(new Item("Zip-Lock Bags (Large)", "Vons", "Kitchen"));

                }
            });
            put("Bathroom", new LinkedList<Item>() {
                {
                    add(new Item("Old Spice Deodorant", "Vons", "Bathroom"));
                    add(new Item("Lavender Hand Soap", "Vons", "Bathroom"));
                    add(new Item("Lavender Body Wash", "Vons", "Bathroom"));
                    add(new Item("Shampoo", "Vons", "Bathroom"));
                    add(new Item("Floss", "Sprouts", "Bathroom"));
                    add(new Item("Toothpaste", "Amazon", "Bathroom"));
                    add(new Item("Shaving Cream", "Amazon", "Bathroom"));
                    add(new Item("Shaving Razors", "Amazon", "Bathroom"));
                    add(new Item("Lavender Laundry Detergent", "Vons", "Bathroom"));
                    add(new Item("Febreeze Air Spray", "Vons", "Bathroom"));

                }
            });
            put("Other", new LinkedList<Item>() {
                {
                    add(new Item("Orange Zig Zags", "7-Eleven", "Other"));
                    add(new Item("Djarum Red", "7-Eleven", "Other"));
                    add(new Item("Visine", "7-Eleven", "Other"));
                    add(new Item("Lighter", "7-Eleven", "Other"));

                }
            });
        }
    };

    private static List<Store> stores = new LinkedList<Store>() {
        {
            add(new Store("Vons", new LinkedList<Item>() {
                {
                    add(new Item("Pasta Sauce (w/ Meat)"));
                    add(new Item("A1 Sauce"));
                    add(new Item("Gnocchi"));
                    add(new Item("Pasta"));
                    add(new Item("Ketchup"));
                    add(new Item("Mustard"));
                    add(new Item("Soy Sauce"));
                    add(new Item("Salted Sunflower Seeds"));
                    add(new Item("Salted Peanuts"));
                    add(new Item("Pepperoni Slices"));
                    add(new Item("Sliced Ham/Turkey"));
                    add(new Item("Meat (Steak/Hamburger/Hot Dog"));
                    add(new Item("Annie's Mac & Cheese"));
                    add(new Item("Semi-Sweet Choc Chips"));
                    add(new Item("Brown Sugar"));
                    add(new Item("Vegetable Oil"));
                    add(new Item("Extra Virgin Olive Oil"));
                    add(new Item("Peanut Butter"));
                    add(new Item("Honey"));
                    add(new Item("Goldfish"));
                    add(new Item("Vinegar Chips"));
                    add(new Item("BBQ Chips"));
                    add(new Item("Dark Chocolate M&M's", "Snack"));
                    add(new Item("Crispy M&M’s"));
                    add(new Item("Hot Tamales"));
                    add(new Item("Soda (Pepsi or Coke)"));
                    add(new Item("Old Spice Deodorant"));
                    add(new Item("Lavender Hand Soap"));
                    add(new Item("Lavender Body Wash"));
                    add(new Item("Shampoo"));
                    add(new Item("Cat Food"));
                    add(new Item("Lavender Laundry Detergent"));
                    add(new Item("Febreeze Air Spray"));
                }
            }));
            add(new Store("Rite Aid", new LinkedList<Item>() {
                {
                    add(new Item("Choc Malted Crunch Ice Cream"));
                }
            }));
            add(new Store("Trader Joe's", new LinkedList<Item>() {
                {
                    add(new Item("Dark Choc Peanut Butter Cups"));
                    add(new Item("Himilayan Salt Shaker"));
                    add(new Item("Rainbow Black Pepper"));
                }
            }));
            add(new Store("Smart & Final", new LinkedList<Item>() {
                {
                    add(new Item("Fun Dip"));
                    add(new Item("Orange Tic Tacs"));
                    add(new Item("Tootsie Rolls"));
                    add(new Item("Oreo Pieces"));
                }
            }));
            add(new Store("Stater Bros", new LinkedList<Item>() {
                {
                    add(new Item("Gluten-Free Choc Chip Cookie Mix"));
                    add(new Item("Malted Milk Mix"));
                }
            }));
            add(new Store("Home Goods", new LinkedList<Item>() {
                {
                    add(new Item("Sixlets"));
                    add(new Item("Smarties"));
                }
            }));
            add(new Store("7-Eleven", new LinkedList<Item>() {
                {
                    add(new Item("Orange Zig Zags"));
                    add(new Item("Djarum Red"));
                    add(new Item("Visine"));
                    add(new Item("Lighter"));
                }
            }));
            add(new Store("Sprouts", new LinkedList<Item>() {
                {
                    add(new Item("Floss"));
                }
            }));
            add(new Store("Amazon", new LinkedList<Item>() {
                {
                    add(new Item("Vanilla Taffy"));
                    add(new Item("Altar Bread Wafers"));
                    add(new Item("Electro-nectar"));
                    add(new Item("Toothpaste"));
                    add(new Item("Shaving Cream"));
                    add(new Item("Shaving Razors"));
                    add(new Item("Oreo Pie Mix"));
                    add(new Item("Malted Milk Mix"));
                }
            }));
            add(new Store("Costco", new LinkedList<Item>() {
                {
                    add(new Item("Sea Salt Caramels"));
                    add(new Item("Teriyaki Beef Jerky"));
                    add(new Item("Cat Litter"));
                }
            }));
            add(new Store("Total Wine & More", new LinkedList<Item>() {
                {
                    add(new Item("Godiva Dark Chocolate Liquor"));
                    add(new Item("Vita Divine Creme de Cacao"));
                    add(new Item("Absolut Vanilla Vodka"));
                }
            }));
            add(new Store("Bevmo", new LinkedList<Item>() {
                {
                    add(new Item("Kavlana Creme de Cacao"));
                }
            }));
        }
    };

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
