using Mapster;
using ShopApp.DTOs;
using ShopApp.Entities;

namespace ShopApp.Mapping;

public class CategoryMapper : IRegister
{
	public void Register(TypeAdapterConfig config)
	{
		config.NewConfig<CreateCategoryDto, Category>()
			.Ignore(src => src.Image);
 
		
	}
}
