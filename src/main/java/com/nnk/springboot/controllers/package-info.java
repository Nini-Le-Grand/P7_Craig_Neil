/**
 * Contains Spring MVC controllers for handling HTTP requests related to the application's business operations.
 *
 * <ul>
 *   <li><b>{@link com.nnk.springboot.controllers.AccessController}</b> - Handles access-related operations such as user registration and login.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.BidListController}</b> - Manages CRUD operations for bid list entries.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.CurvePointController}</b> - Manages CRUD operations for curve point entities.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.CustomErrorController}</b> - Handles error requests by inspecting HTTP error codes and throwing appropriate exceptions.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.ExceptionHandlerController}</b> - Provides centralized exception handling (e.g., for {@code ResponseStatusException}).</li>
 *   <li><b>{@link com.nnk.springboot.controllers.RatingController}</b> - Manages CRUD operations for rating entities.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.RuleNameController}</b> - Manages CRUD operations for rule name entities.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.TradeController}</b> - Manages CRUD operations for trade entities.</li>
 *   <li><b>{@link com.nnk.springboot.controllers.UserController}</b> - Manages CRUD operations for user entities (with routes prefixed by {@code /admin}).</li>
 * </ul>
 */
package com.nnk.springboot.controllers;
