namespace ShopApp.DTOs;

public class EditCategoryDto
{
	public Guid Id { get; set; }

	public required string Name { get; set; }

	public required string Description { get; set; }

	public IFormFile? Image { get; set; }
}
