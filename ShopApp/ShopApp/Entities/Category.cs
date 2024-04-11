namespace ShopApp.Entities;

public class Category
{ 
	public Guid Id { get; set; }

	public required string Name { get; set; }

	public required string Description { get; set; }

	public string? Image { get; set; }

}
