using ErrorOr;
using MapsterMapper;
using ShopApp.DTOs;
using ShopApp.Entities;
using ShopApp.Repositories;

namespace ShopApp.Services;

public class CategoryService(
	CategoryRepositories categoryRepositories, IMapper mapper, ImageStorageService imageStorageService)
{
	public async Task<ErrorOr<Category>> CreateCategoryAsync(CreateCategoryDto model)
	{
		var category = mapper.Map<Category>(model);

		var newCategory = await categoryRepositories.CreateAsync(category);

		if (newCategory == null)
			return Error.Failure("Error in creating");

		if(model.Image != null && model.Image.Length > 0)
		{ 
			await imageStorageService.AddAvatarAsync(newCategory, model.Image!);
		}

		await categoryRepositories.SaveAsync();

		return newCategory;
	}

	public async Task<ErrorOr<Category>> EditCategoryAsync(EditCategoryDto model)
	{
		var category = await categoryRepositories.GetByIdAsync(model.Id);

		if (category == null)
			return Error.NotFound();

		if (category.Name != model.Name || !string.IsNullOrEmpty(model.Name))
		{
			category.Name = model.Name;
		}

		if (category.Description != model.Description || !string.IsNullOrEmpty(model.Description))
		{
			category.Name = model.Name;
		}

		if (model.Image != null && model.Image.Length > 0)
		{ 
			//Todo MAke image edit
		}

		await categoryRepositories.UpdateAsync(category);	

		await categoryRepositories.SaveAsync();

		return category;
	}

	public async Task<ErrorOr<Deleted>> DeleteCategoryByIdAsync(Guid id)
	{
		var category = await categoryRepositories.GetByIdAsync(id);

		if (category == null)
			return Error.NotFound();

		await categoryRepositories.DeleteAsync(category);

		await categoryRepositories.SaveAsync();

		return Result.Deleted;
	}

	public async Task<ErrorOr<Category>> GetCategoryByIdAsync(Guid id)
	{
		var category = await categoryRepositories.GetByIdAsync(id);

		if (category == null)
			return Error.NotFound();

		return category;
	}

	public async Task<ErrorOr<List<Category>>> GetCategorListAsync()
	{ 
		var categoryList = await categoryRepositories.GetListOfCategoriesAsync();

		if (categoryList == null)
			return Error.NotFound();

		return categoryList;
	}
}
