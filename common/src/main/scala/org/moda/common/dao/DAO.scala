package org.moda.common.dao

import org.moda.common.database.HasDatabaseComponent
import org.moda.idl.Page
import slick.ast.Ordering.{Asc, Desc, Direction}
import slick.lifted.{CanBeQueryCondition, Query}


trait DAO extends HasDatabaseComponent with ColumnMapper {

  protected val tables = Tables()

  implicit final class MaybeFilter[E, U](query: Query[E, U, Seq]) {

    def maybeFilter[V, T: CanBeQueryCondition](value: Option[V])(f: (E, V) => T): Query[E, U, Seq] =
      value match {
        case Some(v) => query.withFilter(f(_, v))
        case _ => query
      }

    def maybeIdFilter[T: CanBeQueryCondition](value: Option[Int])(f: (E, Int) => T): Query[E, U, Seq] =
      value match {
        case Some(v) if v > 0 => query.withFilter(f(_, v))
        case _ => query
      }

    def maybeStringFilter[T: CanBeQueryCondition](value: Option[String])(f: (E, String) => T): Query[E, U, Seq] =
      value match {
        case Some(v) if v.trim.nonEmpty => query.withFilter(f(_, v))
        case _ => query
      }

    /**
      * query pagination
      *
      * @param page : Page
      * @return Query[E, U, Seq]
      */
    def paging(page: Page): Query[E, U, Seq] =
      (
        if (page.pageNumber <= 0)
          query
        else
          query.drop((page.pageNumber - 1) * page.pageSize)
        )
        .take(page.pageSize)
  }


  /**
    * 获取order by的列表
    * @param page 分页
    * @return
    */
  def sortSeq(page: Page): Seq[(String, Direction)] = {

    val sortSeq:Seq[(String,Direction)] = page.sort.map(_.map(x => {
      (x.fieldName, if (x.desc) Desc else Asc )
    })).getOrElse(Seq.empty[(String, Direction)])

  sortSeq

  }


}
