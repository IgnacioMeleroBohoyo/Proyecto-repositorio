import { test, expect } from "@playwright/test";

test("home muestra enlace Iniciar sesión cuando no está autenticado", async ({
  page,
}) => {
  await page.goto("/");
  await expect(page.locator("text=Iniciar sesión")).toBeVisible();
});
