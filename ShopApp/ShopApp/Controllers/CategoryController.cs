using Microsoft.AspNetCore.Mvc;
using ShopApp.DTOs;
using ShopApp.Services;

namespace ShopApp.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CategoryController(CategoryService categoryService) : Controller
{
	[HttpPost("/create")]
	public async Task<IActionResult> Create(CreateCategoryDto model)
	{
		var resultOfCategoryCreating = await categoryService.CreateCategoryAsync(model);

		return resultOfCategoryCreating.Match(
			authResult => Ok(resultOfCategoryCreating.Value),
			errors => Problem(errors[0].ToString()));
	}

	[HttpPost("/edit")]
	public async Task<IActionResult> Edit(EditCategoryDto model)
	{
		var resultOfCategoryEdited = await categoryService.EditCategoryAsync(model);

		return resultOfCategoryEdited.Match(
			authResult => Ok(resultOfCategoryEdited.Value),
			errors => Problem(errors[0].ToString()));
	}

	[HttpPost("/delete")]
	public async Task<IActionResult> Delete(Guid id)
	{
		var resultOfCategoryDeleted = await categoryService.DeleteCategoryByIdAsync(id);

		return resultOfCategoryDeleted.Match(
			authResult => Ok(resultOfCategoryDeleted.Value),
			errors => Problem(errors[0].ToString()));
	}

	[HttpGet("/get")]
	public async Task<IActionResult> Get(Guid id)
	{
		var categoryOrError = await categoryService.GetCategoryByIdAsync(id);

		return categoryOrError.Match(
			authResult => Ok(categoryOrError.Value),
			errors => Problem(errors[0].ToString()));
	}

	[HttpGet("/get-list")]
	public async Task<IActionResult> GetList( )
	{
		var categoriesListOrError = await categoryService.GetCategorListAsync();

		return categoriesListOrError.Match(
			authResult => Ok(categoriesListOrError.Value),
			errors => Problem(errors[0].ToString()));
	}
}
